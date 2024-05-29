package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseState

/**
 * The state of a test case.
 * @param T The type of the state value.
 */
@Suppress("UNUSED")
open class TestCaseState<T> protected constructor(value: T?) : ATestCaseState<T>(value) {

    companion object {
        /**
         * Creates a new test case state with the given value.
         */
        fun <T> of(value: T): TestCaseState<T> = TestCaseState(value)

        /**
         * Create a new test case state with a null value
         */
        fun <T> empty(): TestCaseState<T> = TestCaseState(null)
    }

    /**
     * Convert the test case state to a test case result.
     * @param mapper The mapper function that map the state value to the result value.
     * @param R The type of the result value.
     */
    internal fun <R> mapToResult(mapper: (T) -> R): TestCaseResult<R> = TestCaseResult.of(mapper(value))

    /**
     * Apply a function to the value of the test case state.
     * @param fn The function to apply.
     */
    internal fun consumeValue(fn: (T) -> Unit) {
        super.consumeValue(fn)
    }

}