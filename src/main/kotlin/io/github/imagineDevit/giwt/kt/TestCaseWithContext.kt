package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCase
import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.report.TestCaseReport.TestReport
import io.github.imagineDevit.giwt.core.utils.Utils
import io.github.imagineDevit.giwt.kt.statements.functions.CtxConsumer
import io.github.imagineDevit.giwt.kt.statements.functions.ResCtxconsumer

class TestCaseWithContext<T : Any?, R : Any?> internal constructor(
    name: String,
    report: TestReport?,
    parameters: TestParameters.Parameter?
) : ATestCase<T, R, TestCaseCtxState<T>, TestCaseCtxResult<R>>(name, report, parameters) {

    private val gCtx: TestCaseContext.GCtx<T, R> = TestCaseContext.GCtx()
    private lateinit var wCtx: TestCaseContext.WCtx<T, R>
    private lateinit var tCtx: TestCaseContext.TCtx<T, R>

    private val givenFns: MutableList<CtxConsumer<R, TestCaseContext.GCtx<T, R>>> = mutableListOf()
    private lateinit var whenFn: CtxConsumer<R, TestCaseContext.WCtx<T, R>>
    private val thenFns: MutableList<ResCtxconsumer<T, R>> = mutableListOf()

    override fun run() {
        println(Utils.reportTestCase(name, givenMsgs, whenMsgs, thenMsgs, parameters))
        this.givenFns.forEach { it.consume(this.gCtx) }
        this.wCtx = this.gCtx.toWCtx()

        try {
            this.whenFn.consume(this.wCtx)
        } catch (e: Exception) {
            this.wCtx.setResult(TestCaseCtxResult.ofErr(e))
        }

        this.tCtx = this.wCtx.toTCtx()
        this.thenFns.forEach { it.consume(this.tCtx, this.tCtx.getResult()) }
    }

    internal fun play() = run()

    fun given(message: String, givenFn: CtxConsumer<R, TestCaseContext.GCtx<T, R>>): GivenCtxStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.givenFns.add(givenFn)
        GivenCtxStmt(this)
    }

    fun given(message: String, t: T): GivenCtxStmt<T, R> = runIfOpen {
        this.addGivenMsg(message)
        this.gCtx.setState(t)
        GivenCtxStmt(this)
    }

    fun `when`(message: String, whenFn: CtxConsumer<R, TestCaseContext.WCtx<T, R>>): WhenCtxStmt<T, R> = runIfOpen {
        this.addWhenMsg(message)
        this.whenFn = whenFn
        WhenCtxStmt(this)
    }


    inner class GivenCtxStmt<T : Any?, R : Any?>(private val testCase: TestCaseWithContext<T, R>) {

        fun and(message: String, fn: CtxConsumer<R, TestCaseContext.GCtx<T, R>>): GivenCtxStmt<T, R> {
            this.testCase.addAndGivenMsg(message)
            this.testCase.givenFns.add(fn)
            return this
        }

        fun `when`(message: String, whenFn: CtxConsumer<R, TestCaseContext.WCtx<T, R>>): WhenCtxStmt<T, R> {
            this.testCase.addWhenMsg(message)
            this.testCase.whenFn = whenFn
            return WhenCtxStmt(this.testCase)
        }

    }

    inner class WhenCtxStmt<T : Any?, R : Any?>(private val testCase: TestCaseWithContext<T, R>) {

        fun then(message: String, thenFn: ResCtxconsumer<T, R>): ThenCtxStmt<T, R> {
            this.testCase.addThenMsg(message)
            this.testCase.thenFns.add(thenFn)
            return ThenCtxStmt(this.testCase)
        }

    }

    inner class ThenCtxStmt<T : Any?, R : Any?>(private val testCase: TestCaseWithContext<T, R>) {

        fun and(message: String, thenFn: ResCtxconsumer<T, R>): ThenCtxStmt<T, R> {
            this.testCase.addAndThenMsg(message)
            this.testCase.thenFns.add(thenFn)
            return this
        }

    }


}