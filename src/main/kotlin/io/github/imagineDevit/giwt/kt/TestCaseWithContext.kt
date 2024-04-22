package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCase
import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.report.TestCaseReport.TestReport
import io.github.imagineDevit.giwt.core.utils.Utils
import io.github.imagineDevit.giwt.kt.statements.functions.GCtxFn
import io.github.imagineDevit.giwt.kt.statements.functions.TCtxFn
import io.github.imagineDevit.giwt.kt.statements.functions.WCtxFn

/**
 * Test case with context class.
 * @param T The type of the test case state.
 * @param R The type of the test case result.
 */
class TestCaseWithContext<T : Any?, R : Any?> internal constructor(
    name: String,
    report: TestReport?,
    parameters: TestParameters.Parameter?
) : ATestCase<T, R, TestCaseCtxState<T>, TestCaseCtxResult<R>>(name, report, parameters) {

    /**
     * The given statement context.
     */
    private val gCtx: TestCaseContext.GCtx<T, R> = TestCaseContext.GCtx()

    /**
     * The when statement context.
     */
    private lateinit var wCtx: TestCaseContext.WCtx<T, R>

    /**
     * The then statement context.
     */
    private lateinit var tCtx: TestCaseContext.TCtx<T, R>

    /**
     * The list of given statement functions.
     */
    private val givenFns: MutableList<GCtxFn<T, R>> = mutableListOf()

    /**
     * The when statement function.
     */
    private lateinit var whenFn: WCtxFn<T, R>

    /**
     * The list of then statement functions.
     */
    private val thenFns: MutableList<TCtxFn<T, R>> = mutableListOf()

    /**
     * Run the test case.
     */
    override fun run() {
        print(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters))

        this.givenFns.forEach { it(this.gCtx) }

        this.wCtx = this.gCtx.toWCtx()

        try {
            this.whenFn(this.wCtx)
        } catch (e: Exception) {
            this.wCtx.setResult(TestCaseCtxResult.ofErr(e))
        }

        this.tCtx = this.wCtx.toTCtx()

        this.thenFns.forEach { it(this.tCtx) }
    }

    /**
     * Play the test case.
     */
    internal fun play() = run()

    /**
     * Create the given statement for the test case.
     * Adds the given function to the test case.
     * @param message The message of the given statement.
     * @param givenFn The given function.
     */
    fun given(message: String, givenFn: GCtxFn<T, R>): GivenCtxStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.givenFns.add(givenFn)
        GivenCtxStmt(this)
    }

    /**
     * Create the given statement for the test case with a provided state value.
     * @param message The message of the given statement.
     * @param t The provided state value.
     */
    fun given(message: String, t: T): GivenCtxStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.gCtx.setState(t)
        GivenCtxStmt(this)
    }

    /**
     * Create the when statement for the test case.
     * Adds the when function to the test case.
     * @param message The message of the when statement.
     * @param whenFn The when function that supplies the test case result.
     */
    fun `when`(message: String, whenFn: WCtxFn<T, R>): WhenCtxStmt<T, R> = runIfOpen {
        this.addWhenMsg(message)
        this.whenFn = whenFn
        WhenCtxStmt(this)
    }

    /**
     * The given statement class.
     * @param T The type of the test case state.
     * @param R The type of the test case result.
     */
    inner class GivenCtxStmt<T : Any?, R : Any?>(private val testCase: TestCaseWithContext<T, R>) {

        /**
         * Add a given statement to the test case.
         * @param message The message of the given statement.
         * @param fn The given function.
         */
        fun and(message: String, fn: GCtxFn<T, R>): GivenCtxStmt<T, R> {
            this.testCase.addAndGivenMsg(message)
            this.testCase.givenFns.add(fn)
            return this
        }

        /**
         * Create the when statement for the test case.
         * Adds the when function to the test case.
         * @param message The message of the when statement.
         * @param whenFn The when function that supplies the test case result.
         */
        fun `when`(message: String, whenFn: WCtxFn<T, R>): WhenCtxStmt<T, R> {
            this.testCase.addWhenMsg(message)
            this.testCase.whenFn = whenFn
            return WhenCtxStmt(this.testCase)
        }

    }

    /**
     * The when statement class.
     * @param T The type of the test case state.
     * @param R The type of the test case result.
     */
    inner class WhenCtxStmt<T : Any?, R : Any?>(private val testCase: TestCaseWithContext<T, R>) {

        /**
         * Create the then statement for the test case.
         * Adds the then function to the test case.
         * @param message The message of the then statement.
         * @param thenFn The then function that checks the test case result.
         */
        fun then(message: String, thenFn: TCtxFn<T, R>): ThenCtxStmt<T, R> {
            this.testCase.addThenMsg(message)
            this.testCase.thenFns.add(thenFn)
            return ThenCtxStmt(this.testCase)
        }

    }

    /**
     * The then statement class.
     * @param T The type of the test case state.
     * @param R The type of the test case result.
     */
    inner class ThenCtxStmt<T : Any?, R : Any?>(private val testCase: TestCaseWithContext<T, R>) {

        /**
         * Add a then statement to the test case.
         * @param message The message of the then statement.
         * @param thenFn The then function.
         */
        fun and(message: String, thenFn: TCtxFn<T, R>): ThenCtxStmt<T, R> {
            this.testCase.addAndThenMsg(message)
            this.testCase.thenFns.add(thenFn)
            return this
        }

    }


}