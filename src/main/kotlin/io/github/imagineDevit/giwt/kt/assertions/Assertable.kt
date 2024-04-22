package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult


/**
 * Assertable interface that provides a set of methods to assert the value of a Result.
 * @param T The type of the value contained in the Result.
 */
interface Assertable<T>  {

    var sf: ShouldFail?
    var sb: ShouldBe<T>?
    var sh: ShouldHave<T>?
    var sm: ShouldMatch<T>?

    /**
     * Asserts that the Result is an Error.
     *
     * @throws AssertionError If the result value is null.
     * @return A [ShouldFail] object that provides a set of methods to assert the error.
     */
    val shouldFail: ShouldFail
        get() =
            sf ?: value()?.let { value ->
                value.err<Exception>()
                    .map { ShouldFail(it) }
                    .orElseThrow { AssertionError("Result value is Ok") }
            }.also { sf = it } ?: throw AssertionError("Result value is Null")

    /**
     * Build a ShouldBe object from the result value.
     *
     * @return A [ShouldBe] object that provides a set of methods to assert the value.
     */
    val shouldBe: ShouldBe<T>
        get() = sb ?: should { ShouldBe(it).also { sb = it } }


    /**
     * Build a ShouldHave object from the result value.
     *
     * @return A [ShouldHave] object that provides a set of methods to assert the value.
     */
    val shouldHave: ShouldHave<T>
        get() = sh ?: should { ShouldHave(it).also { sh = it } }

    /**
     * Build a ShouldMatch object from the result value.
     *
     * @return A [ShouldMatch] object that provides a set of methods to assert the value.
     */
    val shouldMatch: ShouldMatch<T>
        get() = sm ?: should { ShouldMatch(it).also { sm = it } }

    /**
     * Asserts that the Result is Ok and maps the value to a new type.
     *
     * @param fn The function to map the value.
     * @throws AssertionError If the Result is not Ok.
     * @return The mapped value.
     */
    private fun <T, R> should(fn: (ATestCaseResult.ResultValue.Ok<T>) -> R): R =
        value()?.let {
            it.ok<T>()
                .map { ok -> fn(ok) }
                .orElseThrow { AssertionError("Result value is Failure") }
        } ?: throw AssertionError("Result value is Null")

    /**
     * Gets the value on which the assertions are made.
     */
    fun value(): ATestCaseResult.ResultValue?
}