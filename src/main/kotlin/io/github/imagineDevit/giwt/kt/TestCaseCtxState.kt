package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseState

/**
 * The state of a [TestCaseWithContext].
 * @param T The type of the state value
 */
class TestCaseCtxState<T> private constructor(value: T?) : ATestCaseState<T>(value) {

    companion object {
        /**
         * Creates a new [TestCaseCtxState] with the given value.
         * @param value The value of the state.
         */
        fun <T> of(value: T): TestCaseCtxState<T> {
            return TestCaseCtxState(value)
        }

        /**
         * Creates a new empty [TestCaseCtxState].
         */
        fun <T> empty(): TestCaseCtxState<T> {
            return TestCaseCtxState(null)
        }
    }

    /**
     * Maps the value of the state into a new value.
     * @param mapper The mapper function.
     * @return The new state.
     */
    fun map(mapper: (T) -> T): TestCaseCtxState<T> = TestCaseCtxState(mapper(value))

    /**
     * Maps the value of the state value to the result.
     * @param mapper The mapper function.
     * @return The result of the mapping wrapped into a [TestCaseCtxResult].
     */
    fun <R> toResult(fn: (T) -> R): TestCaseCtxResult<R> {
        return try {
            value?.let { TestCaseCtxResult.of(fn(it)) } ?: TestCaseCtxResult.empty()
        } catch (e: Exception) {
            TestCaseCtxResult.ofErr(e)
        }
    }

    /**
     * Gets the value of the state.
     */
    fun value(): T {
        return value
    }
}