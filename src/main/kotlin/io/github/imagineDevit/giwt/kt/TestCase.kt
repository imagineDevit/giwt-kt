package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCase
import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.report.TestCaseReport.TestReport
import io.github.imagineDevit.giwt.core.utils.Utils
import io.github.imagineDevit.giwt.kt.statements.functions.AndGivenFn
import io.github.imagineDevit.giwt.kt.statements.functions.GivenFn
import io.github.imagineDevit.giwt.kt.statements.functions.ThenFn
import io.github.imagineDevit.giwt.kt.statements.functions.WhenFns

open class TestCase<T : Any?, R : Any?> internal constructor(
    name: String,
    report: TestReport?,
    parameters: TestParameters.Parameter?
) :
    ATestCase<T, R, TestCaseState<T>, TestCaseResult<R>>(name, report, parameters) {

    inner class GivenStmt<T : Any?, R : Any?>(private val testCase: TestCase<T, R>) {

        fun and(message: String, fn: AndGivenFn<T>): GivenStmt<T, R> {
            this.testCase.addAndGivenMsg(message)
            this.testCase.andGivenFns.add(fn)
            return this
        }

        fun `when`(message: String, whenFn: WhenFns.WhenFn<T, R>): WhenStmt<T, R> {
            this.testCase.addWhenMsg(message)
            this.testCase.whenFn = whenFn
            return WhenStmt(this.testCase)
        }

    }

    inner class WhenStmt<T : Any?, R : Any?>(private val testCase: TestCase<T, R>) {

        fun then(message: String, thenFn: ThenFn<R>): ThenStmt<T, R> {
            this.testCase.addThenMsg(message)
            this.testCase.thenFns.add(thenFn)
            return ThenStmt(this.testCase)
        }

    }

    inner class ThenStmt<T : Any?, R : Any?>(private val testCase: TestCase<T, R>) {

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

    private var givenFn: GivenFn<T>? = null
    private val andGivenFns: MutableList<AndGivenFn<T>> = mutableListOf()
    private var whenFn: WhenFns? = null
    private val thenFns: MutableList<ThenFn<R>> = mutableListOf()

    private var ctxCase: TestCaseWithContext<T, R>? = null

    fun given(message: String, givenFn: GivenFn<T>): GivenStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.givenFn = givenFn
        GivenStmt(this)
    }

    fun given(message: String, t: T): GivenStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.state = TestCaseState.of(t)
        GivenStmt(this)
    }

    fun `when`(message: String, whenFn: WhenFns.WhenSFn<R>): WhenStmt<T, R> = runIfOpen {
        this.addWhenMsg(message)
        this.whenFn = whenFn
        WhenStmt(this)
    }

    fun withContext(): TestCaseWithContext<T, R> {
        this.ctxCase = TestCaseWithContext(this.name, this.report, this.parameters)
        return this.ctxCase!!
    }

    internal fun play() = this.ctxCase?.play() ?: run()

    @Suppress("UNCHECKED_CAST")
    override fun run() {

        print(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters))

        this.givenFn?.also {
            this.state = TestCaseState.of(it.apply())
        }

        this.andGivenFns.forEach {
            this.state = this.state.map { s -> it.apply(s) }
        }

        this.whenFn?.also {
            try {
                when (it) {
                    is WhenFns.WhenFn<*, *> -> this.result = this.state.mapToResult { s ->
                        (it as WhenFns.WhenFn<T, R>).apply(s)
                    }

                    is WhenFns.WhenSFn<*> -> this.result = this.state.mapToResult { _ ->
                        (it as WhenFns.WhenSFn<R>).apply()
                    }
                }
            } catch (e: Exception) {
                this.result = TestCaseResult.ofErr(e)
            }
        }

        this.thenFns.forEach {
            it.apply(this.result)
        }
    }
}