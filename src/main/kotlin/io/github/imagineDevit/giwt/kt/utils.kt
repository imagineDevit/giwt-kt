package io.github.imagineDevit.giwt.kt

import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import io.github.imagineDevit.giwt.core.utils.TextUtils
import javax.lang.model.element.TypeElement
import javax.lang.model.type.*
import kotlin.reflect.KClass

fun String.italic(): String = TextUtils.italic(this)
fun String.italicBlue(): String = TextUtils.blue(this.italic())

fun TypeMirror.className(): KClass<*> =
    when (kind) {
        TypeKind.BOOLEAN -> Boolean::class
        TypeKind.BYTE -> Byte::class
        TypeKind.SHORT -> Short::class
        TypeKind.INT -> Int::class
        TypeKind.LONG -> Long::class
        TypeKind.CHAR -> Char::class
        TypeKind.FLOAT -> Float::class
        TypeKind.DOUBLE -> Double::class
        TypeKind.VOID -> Unit::class
        TypeKind.NONE -> Unit::class
        TypeKind.ARRAY -> (this as ArrayType).componentType.className().java.arrayType().kotlin
        TypeKind.DECLARED -> Class.forName(((this as DeclaredType).asElement() as TypeElement).qualifiedName.toString()).kotlin
        TypeKind.WILDCARD -> (this as WildcardType).extendsBound?.className() ?: Any::class
        TypeKind.TYPEVAR -> (this as TypeVariable).upperBound.className()
        else -> Any::class
    }

fun TypeMirror.parameterTypes(): List<TypeName> =
    when (kind) {
        TypeKind.ARRAY -> listOf((this as ArrayType).componentType).map { it.typeName() }
        TypeKind.DECLARED -> (this as DeclaredType).typeArguments.map { it.typeName() }
        TypeKind.WILDCARD -> (this as WildcardType).extendsBound?.parameterTypes() ?: emptyList()
        TypeKind.TYPEVAR -> (this as TypeVariable).upperBound.parameterTypes()
        else -> emptyList()
    }

@OptIn(DelicateKotlinPoetApi::class)
fun TypeMirror.typeName(): TypeName =
    try {
        parameterTypes().takeUnless { it.isEmpty() }?.let {
            this.className().asTypeName().parameterizedBy(it)
        } ?: this.className().asTypeName()
    } catch (e: Exception) {
        this.asTypeName()
    }
