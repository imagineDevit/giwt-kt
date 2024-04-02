package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult

data class ShouldHave<T>(val result: ATestCaseResult.ResultValue.Ok<T>) {

    infix fun size(size: Int): ShouldHave<T> {

        val length = result.value.let {
            when (it) {
                is Collection<*> -> it.size
                is String -> it.length
                else -> throw AssertionError("Result value has no size")
            }
        }

        if (length != size) {
            throw AssertionError("Expected result to have size <$size> but got <$length>")
        }

        return this
    }

    infix fun <R> anItemEqualTo(element: R): ShouldHave<T> {
        result.value.let {
            when (it) {
                is Collection<*> -> {
                    if (!it.contains(element)) {
                        throw AssertionError("Expected result to contain <$element> but it does not")
                    }
                }

                else -> throw AssertionError("Result value is not a collection")
            }
        }

        return this
    }

}
