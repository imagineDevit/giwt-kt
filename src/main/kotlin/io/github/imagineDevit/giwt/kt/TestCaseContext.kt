package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.utils.TextUtils.blue
import io.github.imagineDevit.giwt.core.utils.TextUtils.bold

@Suppress("UNCHECKED_CAST", "unused")
sealed class TestCaseContext<T, R> private constructor(context: MutableMap<String, Any>? = null) {

    internal val context: MutableMap<String, Any> = context ?: HashMap()

    init {
        context?.let { this.context.putAll(it) }
    }

    companion object {
        private val RESULT = "###RESULT###"
        private val STATE = "###STATE###"
    }

    fun setVar(key: String, value: Any) {
        if (key == RESULT || key == STATE) error(
            "$STATE or $RESULT cannot be set by using ${bold(blue("setVar"))}." +
                    "\n Please consider using the dedicated methods ${bold(blue("setState"))} or ${bold(blue("mapState"))} instead."
        )
        this.context[key] = value
    }

    fun <TT> getVar(key: String): TT? {
        if (key == RESULT || key == STATE)
            error("$STATE or $RESULT cannot be accessed by using ${bold(blue("getVar"))} method.")

        return this.context.getOrDefault(key, null) as? TT
    }

    fun <TT> safeGetVar(key: String): TT? {
        return this.context.getOrDefault(key, null) as? TT
    }

    protected fun getState(): TestCaseCtxState<T> {
        return this.context[STATE]?.let { (it as TestCaseCtxState<T>) } ?: TestCaseCtxState.empty()
    }

    class GCtx<T : Any?, R : Any?> : TestCaseContext<T, R>() {

        init {
            this.context[STATE] = TestCaseCtxState.empty<T>()
        }

        fun setState(state: T) {
            this.context[STATE] = TestCaseCtxState.of(state)
        }

        @Suppress("UNCHECKED_CAST")
        fun mapState(fn: (T) -> T) {
            this.context.computeIfPresent(STATE) { _, v -> (v as TestCaseCtxState<T>).map(fn) }
        }


        internal fun toWCtx(): WCtx<T, R> {
            return WCtx(this.context)
        }
    }

    class WCtx<T, R : Any?>(ctx: MutableMap<String, Any> = HashMap()) : TestCaseContext<T, R>(ctx) {

        init {
            this.context[RESULT] = TestCaseCtxResult.empty<R>()
        }

        fun mapToResult(mapper: (T) -> R) = setResult(getState().toResult(mapper))

        fun applyOnState(fn: (T) -> Unit): WCtx<T, R> {
            fn(getState().value())
            return this
        }

        fun setStateAsResult() = setResult(getState().toResult { it as R })

        fun supplyResult(supplier: () -> R) = setResult(TestCaseCtxResult.of(supplier()))

        internal fun toTCtx(): TCtx<T, R> {
            return TCtx(this.context)
        }

        internal fun setResult(value: TestCaseCtxResult<R>) {
            this.context[RESULT] = value
        }

    }

    class TCtx<T, R>(ctx: MutableMap<String, Any> = HashMap()) : TestCaseContext<T, R>(ctx) {
        internal fun getResult(): TestCaseResult<R> {
            return safeGetVar<TestCaseCtxResult<R>>(RESULT)?.result() ?: TestCaseResult.empty()
        }
    }
}