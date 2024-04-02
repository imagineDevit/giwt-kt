package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult
import io.github.imagineDevit.giwt.core.utils.TextUtils
import java.util.function.Predicate

@Suppress("unused")
data class ShouldMatch<T>(val result: ATestCaseResult.ResultValue.Ok<T>) {
    data class Matching<T>(val description: String, val predicate: Predicate<T>) {
        fun shouldTest(value: T) {
            if (!predicate.test(value)) {
                throw AssertionError("Matching < ${TextUtils.yellow(description)} > failed ")
            }
        }
    }

    fun <T> matching(description: String, predicate: Predicate<T>): Matching<T> = Matching(description, predicate)

    infix fun one(matching: Matching<T>): ShouldMatch<T> = matching.shouldTest(result.value).let { this }

    fun one(description: String, predicate: Predicate<T>): ShouldMatch<T> = one(matching(description, predicate))

    fun all(vararg matchings: Matching<T>): ShouldMatch<T> =
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

    fun none(vararg matchings: Matching<T>): ShouldMatch<T> =
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
