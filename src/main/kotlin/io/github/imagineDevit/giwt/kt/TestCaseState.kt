package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseState

@Suppress("UNUSED")
open class TestCaseState<T> protected constructor(value: T?) : ATestCaseState<T>(value) {

    companion object {
        fun <T> of(value: T): TestCaseState<T> = TestCaseState(value)

        fun <T> empty(): TestCaseState<T> = TestCaseState(null)
    }

    internal fun map(mapper: (T) -> T): TestCaseState<T> = TestCaseState(mapper(value))

    internal fun <R> mapToResult(mapper: (T) -> R): TestCaseResult<R> = TestCaseResult.of(mapper(value))


}