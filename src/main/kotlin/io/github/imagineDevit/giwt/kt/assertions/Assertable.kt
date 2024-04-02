package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult

abstract class Assertable<T> : ATestCaseResult<T> {

    protected constructor(value: T?) : super(value)

    protected constructor(e: Exception) : super(e)

    val shouldFail: ShouldFail
        get() =
            value()?.let {
                it.err<Exception>()
                    .map { ShouldFail(it) }
                    .orElseThrow { AssertionError("Result value is Ok") }

            } ?: throw AssertionError("Result value is Null")

    val shouldBe: ShouldBe<T> get() = should { ShouldBe(it) }

    val shouldHave: ShouldHave<T> get() = should { ShouldHave(it) }

    val shouldMatch: ShouldMatch<T> get() = should { ShouldMatch(it) }

    private fun <T, R> should(fn: (ResultValue.Ok<T>) -> R): R =
        value()?.let {
            it.ok<T>()
                .map { ok -> fn(ok) }
                .orElseThrow { AssertionError("Result value is Failure") }
        } ?: throw AssertionError("Result value is Null")

    protected abstract fun value(): ResultValue?
}