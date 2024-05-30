package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseResult
import io.github.imagineDevit.giwt.core.utils.MutVal
import io.github.imagineDevit.giwt.kt.expectations.KExpectable

/**
 * The result of a test case.
 * @param T The type of the result value.
 */
@Suppress("UNUSED")
open class TestCaseResult<T> : KExpectable<T>, ATestCaseResult<T> {

    private val rValue = MutVal<T>()
    private val rError = MutVal<Throwable>()

    protected constructor(value: T?) : super(value)

    protected constructor(e: Throwable) : super(e)

    companion object {

        /**
         * Creates a new [TestCaseResult] with a given value.
         */
        internal fun <T> of(value: T?): TestCaseResult<T> = value?.let { TestCaseResult(it) } ?: empty()

        /**
         * Creates a new [TestCaseResult] with a given exception.
         */
        internal fun <T> ofErr(e: Throwable): TestCaseResult<T> = TestCaseResult(e)

        /**
         * Creates a new [TestCaseResult] with a null value.
         */
        internal fun <T> empty(): TestCaseResult<T> = TestCaseResult(null)
    }

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


    override fun resultValue(): T = rValue.getOr {
        (value ?: error("Result value is null")).ok<T>()
            .orElseThrow { error("Result is Failure") }
            .value
    }

    override fun resultError(): Throwable = rError.getOr {
        (value ?: error("Result value is null")).err<Throwable>()
            .orElseThrow { error("Result is Ok") }
            .error
    }
}