package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.kt.assertions.Assertable

@Suppress("UNUSED")
open class TestCaseResult<T> : Assertable<T> {

    protected constructor(value: T?) : super(value)

    protected constructor(e: Exception) : super(e)

    companion object {
        internal fun <T> of(value: T?): TestCaseResult<T> = value?.let { TestCaseResult(it) } ?: empty()

        internal fun <T> ofErr(e: Exception): TestCaseResult<T> = TestCaseResult(e)

        internal fun <T> empty(): TestCaseResult<T> = TestCaseResult(null)
    }

    fun <R> map(mapper: (T) -> R): TestCaseResult<R> {
        return value.ok<T>()
            .map<T> { it.value }
            .map { of(mapper(it)) }
            .orElseThrow { error("Result is Failure") }
    }

    override fun value(): ResultValue = this.value

}