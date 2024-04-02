package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseResult

class TestCaseCtxResult<R> : ATestCaseResult<R> {

    private constructor(value: R?) : super(value)

    private constructor(e: Exception) : super(e)

    companion object {
        internal fun <R> of(value: R): TestCaseCtxResult<R> {
            return TestCaseCtxResult(value)
        }

        internal fun <R> ofErr(e: Exception): TestCaseCtxResult<R> {
            return TestCaseCtxResult(e)
        }

        internal fun <R> empty(): TestCaseCtxResult<R> {
            return TestCaseCtxResult(null)
        }
    }

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