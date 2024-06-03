package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseResult
import io.github.imagineDevit.giwt.core.utils.MutVal
import io.github.imagineDevit.giwt.kt.expectations.KExpectable
import java.util.*

/**
 * The result of a test case.
 * @param R The type of the result value.
 */
@Suppress("UNUSED")
open class TestCaseResult<R> : KExpectable<R>, ATestCaseResult<R> {

    private val rValue = MutVal<R>()
    private val rError = MutVal<Throwable>()

    protected constructor(value: R?) : super(value)

    protected constructor(e: Throwable) : super(e)

    companion object {

        /**
         * Creates a new [TestCaseResult] with a given value.
         */
        internal fun <R> of(value: R?): TestCaseResult<R> = TestCaseResult(value)

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
    fun <S> map(mapper: (R) -> S): TestCaseResult<S> {
        return value.ok<R>()
            .map { of(mapper(it.value)) }
            .orElseThrow { error("Result is Failure") }
    }

    override fun resultValue(): R = rValue.getOr {
        (value ?: error("Result value is null")).ok<R>()
            .orElseThrow { error("Result is Failure") }
            .value
    }

    override fun resultError(): Throwable = rError.getOr {
        (value ?: error("Result value is null")).err<Throwable>()
            .orElseThrow { error("Result is Success") }
            .error
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return Objects.equals(value, (other as TestCaseResult<*>).value)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(value)
    }

}