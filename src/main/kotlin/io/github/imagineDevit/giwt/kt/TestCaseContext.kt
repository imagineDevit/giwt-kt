package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.utils.TextUtils.blue
import io.github.imagineDevit.giwt.core.utils.TextUtils.bold

/**
 * The context of a test case.
 * @param T The type of the test case state.
 * @param R The type of the test case result.
 */
@Suppress("UNCHECKED_CAST", "unused")
sealed class TestCaseContext<T, R> private constructor(context: MutableMap<String, Any>? = null) {

    /**
     * The context key-value store.
     * This store is used to store the state and the result of the test case.
     * Others variables can also be stored in this store.
     */
    internal val context: MutableMap<String, Any> = context ?: HashMap()

    init {
        context?.let { this.context.putAll(it) }
    }

    companion object {
        private val RESULT = "###RESULT###"
        private val STATE = "###STATE###"
    }

    /**
     * Sets a variable in the context.
     * This function must not be used to set the state or the result of the test case.
     * For that purpose, please use the dedicated methods [GCtx.setState] for the state and [WCtx.setResult] for the result.
     * @param key The key of the variable.
     * @param value The value of the variable.
     */
    fun setVar(key: String, value: Any) {
        if (key == RESULT || key == STATE) error(
            "$STATE or $RESULT cannot be set by using ${bold(blue("setVar"))}." +
                    "\n Please consider using the dedicated methods ${bold(blue("setState"))} or ${bold(blue("mapState"))} instead."
        )
        this.context[key] = value
    }

    /**
     * Gets a variable from the context.
     * This function must not be used to get the state or the result of the test case.
     * For that purpose, please use the dedicated methods [getState] for the state and [TCtx.result] for the result.
     * @param key The key of the variable.
     * @return The value of the variable.
     */
    fun <TT> getVar(key: String): TT? {
        if (key == RESULT || key == STATE)
            error("$STATE or $RESULT cannot be accessed by using ${bold(blue("getVar"))} method.")

        return this.safeGetVar(key)
    }

    /**
     * Gets a variable from the context without checks teh key.
     * @param key The key of the variable.
     * @return The value of the variable.
     */
    protected fun <TT> safeGetVar(key: String): TT? {
        return this.context.getOrDefault(key, null) as? TT
    }

    /**
     * Gets the state of the test case.
     * If the state is not set, an empty [TestCaseCtxState] is returned.
     * @return The state of the test case.
     */
    protected fun getState(): TestCaseCtxState<T> {
        return this.context[STATE]?.let { (it as TestCaseCtxState<T>) } ?: TestCaseCtxState.empty()
    }

    /**
     * Given statement context
     */
    class GCtx<T : Any?, R : Any?> : TestCaseContext<T, R>() {

        /**
         * On init, the state is set to an empty state.
         */
        init {
            this.context[STATE] = TestCaseCtxState.empty<T>()
        }

        /**
         * Sets the state of the test case.
         * @param state The value of the test case state.
         */
        fun setState(state: T) {
            this.context[STATE] = TestCaseCtxState.of(state)
        }

        /**
         * Maps the state of the test case into a new value.
         * @param fn The function to map the state.
         */
        @Suppress("UNCHECKED_CAST")
        fun mapState(fn: (T) -> T) {
            this.context.computeIfPresent(STATE) { _, v -> (v as TestCaseCtxState<T>).map(fn) }
        }


        /**
         * Convert the context to a [WCtx] context.
         */
        internal fun toWCtx(): WCtx<T, R> {
            return WCtx(this.context)
        }
    }

    /**
     * When statement context
     */
    class WCtx<T, R : Any?>(ctx: MutableMap<String, Any> = HashMap()) : TestCaseContext<T, R>(ctx) {

        /**
         * On init, the result is set to an empty result.
         */
        init {
            this.context[RESULT] = TestCaseCtxResult.empty<R>()
        }

        /**
         * Map the test case state to result.
         * @param mapper The function to map the state to the result.
         */
        fun mapToResult(mapper: (T) -> R) = setResult(getState().toResult(mapper))

        /**
         * Apply a function on the test case state.
         * @param fn The function to apply on the state.
         */
        fun applyOnState(fn: (T) -> Unit) {
            fn(getState().value())
        }

        /**
         * Set the state as the result.
         *
         * @throws ClassCastException If the state is not of the result type.
         */
        fun setStateAsResult() = setResult(getState().toResult { it as R })

        /**
         * Supply the result of the test case.
         * @param supplier The supplier of the result.
         */
        fun supplyResult(supplier: () -> R) = setResult(TestCaseCtxResult.of(supplier()))

        /**
         * Convert the context to a [TCtx] context.
         */
        internal fun toTCtx(): TCtx<T, R> {
            return TCtx(this.context)
        }

        /**
         * Put the result into the context store.
         */
        internal fun setResult(value: TestCaseCtxResult<R>) {
            this.context[RESULT] = value
        }

    }

    /**
     * Then statement context
     */
    class TCtx<T, R>(ctx: MutableMap<String, Any> = HashMap()) : TestCaseContext<T, R>(ctx) {


        /**
         * An utility function that allow to write a dsl-like code to assert the result.
         *
         * Example:
         * ```
         * result { shouldBe  equalTo 2 }
         * ```
         */
        fun result(fn: TestCaseResult<R>.() -> Unit) {
            fn(getResult())
        }

        /**
         * Get the test case result from the context store.
         */
        private fun getResult(): TestCaseResult<R> {
            return safeGetVar<TestCaseCtxResult<R>>(RESULT)?.result() ?: TestCaseResult.empty()
        }

    }
}