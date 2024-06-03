package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseResult
import java.util.*

/**
 * The result of a [TestCaseWithContext].
 * @param R The type of the result value.
 */
class TestCaseCtxResult<R> : ATestCaseResult<R> {

    private constructor(value: R?) : super(value)

    private constructor(e: Throwable) : super(e)

    companion object {

        /**
         * Creates a new [TestCaseCtxResult] with the given value.
         * @param value The value of the result.
         */
        internal fun <R> of(value: R?): TestCaseCtxResult<R> = TestCaseCtxResult(value)


        /**
         * Creates a new [TestCaseCtxResult] with the given error.
         * @param e The error of the result.
         */
        internal fun <R> ofErr(e: Throwable): TestCaseCtxResult<R> = TestCaseCtxResult(e)


        /**
         * Creates a new empty [TestCaseCtxResult].
         */
        internal fun <R> empty(): TestCaseCtxResult<R> = TestCaseCtxResult(null)

    }

    /**
     * Create a [TestCaseResult] from the current result value.
     * @return The resulting [TestCaseResult].
     * @throws IllegalStateException If the result is not a value or an error.
     */
    fun result(): TestCaseResult<R> = value.ok<R>()
        .map { TestCaseResult.of(it.value) }
        .orElseGet {
            TestCaseResult.ofErr(
                value.err<Throwable>()
                    .map { it.error }
                    .orElseThrow { error("Unexpected error occurred while mapping result to TestCaseResult") }
            )
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return Objects.equals(value, (other as TestCaseCtxResult<*>).value)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(value)
    }
}