package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseResult
import io.github.imagineDevit.giwt.kt.assertions.*

/**
 * The result of a test case.
 * @param T The type of the result value.
 */
@Suppress("UNUSED")
open class TestCaseResult<T> : Assertable<T>, ATestCaseResult<T> {

    override var sf: ShouldFail? = null
    override var sb: ShouldBe<T>?= null
    override var sh: ShouldHave<T>? = null
    override var sm: ShouldMatch<T>? = null

    protected constructor(value: T?) : super(value)

    protected constructor(e: Exception) : super(e)

    companion object {

        /**
         * Creates a new [TestCaseResult] with a given value.
         */
        internal fun <T> of(value: T?): TestCaseResult<T> = value?.let { TestCaseResult(it) } ?: empty()

        /**
         * Creates a new [TestCaseResult] with a given exception.
         */
        internal fun <T> ofErr(e: Exception): TestCaseResult<T> = TestCaseResult(e)

        /**
         * Creates a new [TestCaseResult] with a null value.
         */
        internal fun <T> empty(): TestCaseResult<T> = TestCaseResult(null)
    }

    /**
     * @return The value of the result.
     * @see Assertable.value()
     */
    override fun value(): ResultValue = this.value

    /**
     * Maps the value of the result to a new type.
     * @throws IllegalStateException If the result is a Failure.
     * @param mapper The function to map the value.
     */
    fun <R> map(mapper: (T) -> R): TestCaseResult<R> {
        return value.ok<T>()
            .map<T> { it.value }
            .map { of(mapper(it)) }
            .orElseThrow { error("Result is Failure") }
    }

    /**
     * An utility function that allow to write a dsl-like code to assert the result.
     *
     * Example:
     * ```
     * result { shouldBe  equalTo 2 }
     * ```
     */
    fun result(fn: TestCaseResult<T>.() -> Unit) {
        this.fn()
    }
}