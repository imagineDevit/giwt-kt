package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseState

class TestCaseCtxState<T> private constructor(value: T?) : ATestCaseState<T>(value) {

    companion object {
        fun <T> of(value: T): TestCaseCtxState<T> {
            return TestCaseCtxState(value)
        }

        fun <T> empty(): TestCaseCtxState<T> {
            return TestCaseCtxState(null)
        }
    }

    fun map(mapper: (T) -> T): TestCaseCtxState<T> = TestCaseCtxState(mapper(value))

    fun <R> toResult(fn: (T) -> R): TestCaseCtxResult<R> {
        return try {
            value?.let { TestCaseCtxResult.of(fn(it)) } ?: TestCaseCtxResult.empty()
        } catch (e: Exception) {
            TestCaseCtxResult.ofErr(e)
        }
    }

    fun value(): T {
        return value
    }
}