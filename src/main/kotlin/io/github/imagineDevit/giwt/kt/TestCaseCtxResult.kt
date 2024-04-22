package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseResult

/**
 * The result of a [TestCaseWithContext].
 * @param R The type of the result value.
 */
class TestCaseCtxResult<R> : ATestCaseResult<R> {

    private constructor(value: R?) : super(value)

    private constructor(e: Exception) : super(e)

    companion object {

        /**
         * Creates a new [TestCaseCtxResult] with the given value.
         * @param value The value of the result.
         */
        internal fun <R> of(value: R): TestCaseCtxResult<R> {
            return TestCaseCtxResult(value)
        }

        /**
         * Creates a new [TestCaseCtxResult] with the given error.
         * @param e The error of the result.
         */
        internal fun <R> ofErr(e: Exception): TestCaseCtxResult<R> {
            return TestCaseCtxResult(e)
        }

        /**
         * Creates a new empty [TestCaseCtxResult].
         */
        internal fun <R> empty(): TestCaseCtxResult<R> {
            return TestCaseCtxResult(null)
        }
    }

    /**
     * Create a [TestCaseResult] from the current result value.
     * @return The resulting [TestCaseResult].
     * @throws IllegalStateException If the result is not a value or an error.
     */
    fun result(): TestCaseResult<R> = value.ok<R>()
        .map<R> { it.value }
        .map { TestCaseResult.of(it) }
        .orElseGet {
            TestCaseResult.ofErr(
                value.err<Exception>()
                    .map { it.error }
                    .orElseThrow { error("Unexpected error occurred while mapping result to TestCaseResult") }
            )
        }
}