package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult

/**
 * ShouldMatch class that provides a set of methods to assert the value of a Result.Ok.
 *
 * @param T The type of the value contained in the Result.
 * @see ATestCaseResult.ResultValue
 */
@Suppress("unused")
data class ShouldMatch<T>(val result: ATestCaseResult.ResultValue.Ok<T>) {



    /**
     * Asserts that the value matches a given predicate
     *
     * @param matching The predicate to test the value.
     * @throws AssertionError If the value does not match the predicate.
     */
    infix fun one(matching: Matching<T>): ShouldMatch<T> = matching.shouldTest(result.value).let { this }

    /**
     * Asserts that the value matches all given predicates
     *
     * @param matchings The predicates to test the value.
     * @throws AssertionError If the value does not match any of the predicates.
     */
    infix fun all(matchings: Array<Matching<T>>): ShouldMatch<T> =
        matchings
            .filter { !it.predicate.test(result.value) }
            .map { "👉 ${it.description}" }
            .let {
                if (it.isNotEmpty()) {
                    it.joinToString("\n")
                    throw AssertionError("\n Following matchings failed: \n ${it.joinToString("\n")}  \n")
                }
                this
            }

    /**
     * Asserts that the value does not match any of the given predicates
     *
     * @param matchings The predicates to test the value.
     * @throws AssertionError If the value matches any of the predicates.
     */
    infix fun none(matchings: Array<Matching<T>>): ShouldMatch<T> =
        matchings
            .filter { it.predicate.test(result.value) }
            .map { "👉 ${it.description}" }
            .let {
                if (it.isNotEmpty()) {
                    it.joinToString("\n")
                    throw AssertionError(
                        "\n Following matchings are expected to fail but succeed: \n ${
                            it.joinToString(
                                "\n"
                            )
                        }  \n"
                    )
                }
                this
            }

}

