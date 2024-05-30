package io.github.imagineDevit.giwt.kt.processors


import com.squareup.kotlinpoet.*
import io.github.imagineDevit.giwt.core.annotations.ParametersDataName
import io.github.imagineDevit.giwt.kt.italic
import io.github.imagineDevit.giwt.kt.italicBlue
import io.github.imagineDevit.giwt.kt.typeName
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.Elements

private const val COMPLETION = "\$completion"
private const val DELEGATE = "delegateInstance"
private const val INIT = "<init>"
private const val CL_INIT = "<clinit>"
private const val PARAMS = "Params"
private const val P0 = "p0"
private const val TESTER = "TestProxy"


/**
 * Processor to generate a proxy class for a given class annotated with [io.github.imagineDevit.giwt.core.annotations.TestProxy]
 * @see io.github.imagineDevit.giwt.core.annotations.TestProxy
 * @author Henri Joel SEDJAME
 */

@SupportedAnnotationTypes("io.github.imagineDevit.giwt.core.annotations.TestProxy")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class TestProxyProcessor : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements

    private var dataClassesNames = mutableListOf<String>()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        annotations
            .forEach { annotation -> roundEnv.getElementsAnnotatedWith(annotation).forEach { buildFile(it) } }

        return false
    }

    @OptIn(DelicateKotlinPoetApi::class)
    private fun buildFile(element: Element) {

        dataClassesNames.clear()

        val packageName = elementUtils.getPackageOf(element).toString()
        val delegateName = element.simpleName
        val fileName = "$delegateName$TESTER"
        val file = FileSpec.builder(packageName, fileName)

        val classBuilder = TypeSpec.classBuilder(fileName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(DELEGATE, element.asType().asTypeName())
                    .build()
            )
            .addProperty(
                PropertySpec.builder(DELEGATE, element.asType().asTypeName())
                    .initializer(DELEGATE)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )

        val getters = element.enclosedElements
            .filterIsInstance<VariableElement>()
            .map { "get${it.simpleName.toString().replaceFirstChar { c -> c.uppercaseChar() }}" }

        val discarded = element.enclosedElements
            .filterIsInstance<ExecutableElement>()
            .mapNotNull { it.getAnnotation(JvmName::class.java) }
            .map { it.name }

        val isValid: (ExecutableElement) -> Boolean = {
            it.simpleName.toString() !in getters
                    && it.simpleName.toString() !in discarded
                    && it.simpleName.toString() != INIT
                    && it.simpleName.toString() != CL_INIT
                    && it.modifiers.contains(Modifier.PUBLIC)
        }

        element.enclosedElements
            .filterIsInstance<ExecutableElement>()
            .filter { isValid(it) }
            .forEach { classBuilder.addFunction(buildFunc(classBuilder, it)) }

        file
            .addType(classBuilder.build())
            .build()
            .writeTo(processingEnv.filer)
    }

    private fun buildFunc(classBuilder: TypeSpec.Builder, element: ExecutableElement): FunSpec {

        val elementName = element.simpleName.toString()
        val builder = FunSpec.builder(elementName)

        // Check if the function is a suspend function
        val isSuspend: (VariableElement) -> Boolean = { it.simpleName.toString() == COMPLETION }

        val continuationParam = element.parameters.find { isSuspend(it) }

        // Add suspend modifier if the function has a suspend parameter
        val isSuspended = continuationParam != null

        var returnType = element.returnType.typeName()

        if (isSuspended) {
            builder.addModifiers(KModifier.SUSPEND)
            returnType = (continuationParam!!.asType() as DeclaredType).typeArguments.first().let {
                "$it".split(" ").last().let { s -> Class.forName(s).kotlin.asTypeName() }
            }
        }

        val parameters = element.parameters.filter { !isSuspend(it) }

        when {
            parameters.isEmpty() -> {
                builder.addStatement("return this.${DELEGATE}.${element.simpleName}()")
            }

            parameters.size == 1 -> {
                builder.addParameter(parameters[0].simpleName.toString(), parameters[0].asType().typeName())
                builder.addStatement("return this.${DELEGATE}.${element.simpleName}(${parameters[0].simpleName})")
            }

            else -> {

                val dataClassName = (element.getAnnotation(ParametersDataName::class.java)?.value
                    ?: "${element.simpleName}").replaceFirstChar { c -> c.uppercaseChar() } + PARAMS

                if (dataClassesNames.contains(dataClassName)) {
                    throw IllegalArgumentException(
                        """
                        Cannot generate ${"parameters data class".italic()} with an already used name ${dataClassName.italicBlue()} for the overloaded method ${elementName.italicBlue()}.    
                        Please consider to use the ${"@ParametersDataName".italicBlue()} annotation on the overloaded method to specify a different name for ${"the generated parameters data class".italic()}.
                    """.trimIndent()
                    )
                } else {
                    dataClassesNames.add(dataClassName)
                }

                val dataClass = buildDataClass(dataClassName, parameters)

                classBuilder.addType(dataClass)

                val args = dataClass.propertySpecs.joinToString { "$P0.${it.name}" }

                builder.addParameter(
                    ParameterSpec.builder(
                        P0,
                        ClassName.bestGuess(dataClassName)
                    ).build()

                )
                builder.addStatement("return this.${DELEGATE}.${element.simpleName}($args)")
            }
        }

        return builder
            .returns(returnType)
            .build()
    }

    private fun buildDataClass(name: String, parameters: List<VariableElement>): TypeSpec =
        TypeSpec
            .classBuilder(name)
            .addModifiers(KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameters(
                        parameters.map {
                            ParameterSpec.builder(
                                it.simpleName.toString(),
                                it.asType().typeName()
                            ).build()
                        }
                    )
                    .build()
            )
            .addProperties(
                parameters.map {
                    PropertySpec.builder(
                        it.simpleName.toString(),
                        it.asType().typeName()
                    )
                        .initializer(it.simpleName.toString())
                        .addModifiers(KModifier.PUBLIC)
                        .build()
                }
            ).build()

}