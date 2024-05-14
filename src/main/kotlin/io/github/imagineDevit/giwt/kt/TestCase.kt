package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCase
import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.report.TestCaseReport.TestReport
import io.github.imagineDevit.giwt.core.utils.Utils
import io.github.imagineDevit.giwt.kt.statements.functions.AndGivenFn
import io.github.imagineDevit.giwt.kt.statements.functions.GivenFn
import io.github.imagineDevit.giwt.kt.statements.functions.ThenFn
import io.github.imagineDevit.giwt.kt.statements.functions.WhenFns
import kotlinx.coroutines.runBlocking

/**
 * The Kotlin implementation of the Giwt test case.
 */
open class TestCase<T : Any?, R : Any?> internal constructor(
    name: String,
    report: TestReport?,
    parameters: TestParameters.Parameter?
) :
    ATestCase<T, R, TestCaseState<T>, TestCaseResult<R>>(name, report, parameters) {

    /**
     * The given statement.
     */
    inner class GivenStmt<T : Any?, R : Any?>(private val testCase: TestCase<T, R>) {

        /**
         * Adds a given statement function to the existing given statement.
         * @param message The message of the given statement.
         * @param fn The given statement function.
         * @return The current given statement.
         */
        fun and(message: String, fn: AndGivenFn<T>): GivenStmt<T, R> {
            this.testCase.addAndGivenMsg(message)
            this.testCase.andGivenFns.add(fn)
            return this
        }

        /**
         * Create a when statement from the current given statement.
         * Adds the function associated with the when statement to the test case.
         * @param message The message of the when statement.
         * @param whenFn The when statement function.
         * @return The created when statement.
         */
        fun `when`(message: String, whenFn: WhenFns.WhenFn<T, R>): WhenStmt<T, R> {
            this.testCase.addWhenMsg(message)
            this.testCase.whenFn = whenFn
            return WhenStmt(this.testCase)
        }

    }

    /**
     * The when statement.
     */
    inner class WhenStmt<T : Any?, R : Any?>(private val testCase: TestCase<T, R>) {

        /**
         * Create a then statement from the current when statement.
         * Adds the function associated with the then statement to the test case.
         * @param message The message of the then statement.
         * @param thenFn The then statement function.
         * @return The created then statement.
         */
        fun then(message: String, thenFn: ThenFn<R>): ThenStmt<T, R> {
            this.testCase.addThenMsg(message)
            this.testCase.thenFns.add(thenFn)
            return ThenStmt(this.testCase)
        }

    }

    /**
     * The then statement.
     */
    inner class ThenStmt<T : Any?, R : Any?>(private val testCase: TestCase<T, R>) {

        /**
         * Adds a then statement function to the existing then statement.
         * @param message The message of the then statement.
         * @param thenFn The then statement function.
         */
        fun and(message: String, thenFn: ThenFn<R>): ThenStmt<T, R> {
            this.testCase.addAndThenMsg(message)
            this.testCase.thenFns.add(thenFn)
            return this
        }
    }

    init {
        this.state = TestCaseState.empty()
        this.result = TestCaseResult.empty()
    }

    /**
     * The given function.
     * This function provide the initial state of the test case.
     */
    private var givenFn: GivenFn<T>? = null

    /**
     * The list of and given functions.
     * These functions are used to mutate the state of the test case
     * or to add additional given statements such as defining mocks or stubs behaviors.
     */
    private val andGivenFns: MutableList<AndGivenFn<T>> = mutableListOf()

    /**
     * The when function.
     * This function is used to apply the action that is tested.
     * It can be a function that takes the state as a parameter and returns the result
     * or a supplier that returns the result.
     * @see WhenFns
     */
    private lateinit var whenFn: WhenFns

    /**
     * The list of then functions.
     * These functions are used to assert the result of the test case.
     * @see ThenFn
     */
    private val thenFns: MutableList<ThenFn<R>> = mutableListOf()

    /**
     * The test case with context
     * @see TestCaseWithContext
     */
    private var ctxCase: TestCaseWithContext<T, R>? = null

    /**
     * Create the given statement for the test case.
     * Adds the given function to the test case.
     * @param message The message of the given statement.
     * @param givenFn The given function.
     */
    fun given(message: String, givenFn: GivenFn<T>): GivenStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.givenFn = givenFn
        GivenStmt(this)
    }

    /**
     * Create the given statement for the test case.
     * Adds the given function to the test case.
     * @param message The message of the given statement.
     * @param value The provided state value
     */
    fun given(message: String, value: T): GivenStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.state = TestCaseState.of(value)
        GivenStmt(this)
    }

    /**
     * Create the when statement for the test case.
     * Adds the when function to the test case.
     * @param message The message of the when statement.
     * @param whenFn The when function that supplies the test case result.
     */
    fun `when`(message: String, whenFn: WhenFns.WhenSFn<R>): WhenStmt<T, R> = runIfOpen {
        this.addWhenMsg(message)
        this.whenFn = whenFn
        WhenStmt(this)
    }

    /**
     * Create a [TestCaseWithContext] from the current test case.
     * @return The test case with context.
     */
    fun withContext(): TestCaseWithContext<T, R> {
        this.ctxCase = TestCaseWithContext(this.name, this.report, this.parameters)
        return this.ctxCase!!
    }

    internal fun play() = this.ctxCase?.play() ?: run()

    @Suppress("UNCHECKED_CAST")
    override fun run() {

        print(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters))

        this.givenFn?.also {
            this.state = TestCaseState.of(it())
        }

        this.andGivenFns.forEach {
            this.state.apply(it)
        }

        try {
            when (this.whenFn) {
                is WhenFns.WhenFn<*, *> -> this.result = this@TestCase.state.mapToResult { s ->
                    runBlocking { (this@TestCase.whenFn as WhenFns.WhenFn<T, R>).invoke(s) }
                }

                is WhenFns.WhenSFn<*> -> this.result = this.state.mapToResult { _ ->
                    runBlocking { (this@TestCase.whenFn as WhenFns.WhenSFn<R>).invoke() }
                }
            }
        } catch (e: Exception) {
            this.result = TestCaseResult.ofErr(e)
        }


        this.thenFns.forEach { fn ->
            fn.invoke(this.result)
        }
    }
}