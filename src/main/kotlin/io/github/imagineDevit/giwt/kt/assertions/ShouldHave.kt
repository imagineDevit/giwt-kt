package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult

/**
 * ShouldHave class that provides a set of methods to assert the value of a Result.Ok.
 * @param T The type of the value contained in the Result.
 *
 * @see ATestCaseResult.ResultValue
 */
data class ShouldHave<T>(val result: ATestCaseResult.ResultValue.Ok<T>) {


    /**
     * Asserts that the value has an expected size.
     * Only works with collections, maps and strings.
     *
     * @param size The expected size.
     *
     * @throws IllegalStateException If the value is not a collection, map or string.
     * @throws AssertionError If the value has a different size.
     */
    infix fun size(size: Int): ShouldHave<T> {

        val length = result.value.let {
            when (it) {
                is Collection<*> -> it.size
                is Array<*> -> it.size
                is Map<*, *> -> it.size
                is String -> it.length
                else -> throw IllegalStateException("Result value has no size")
            }
        }

        if (length != size) {
            throw AssertionError("Expected result to have size <$size> but got <$length>")
        }

        return this
    }

    /**
     * Asserts that the value contains an expected element.
     * Only works with collections.
     *
     * @param element The expected element.
     *
     * @throws IllegalStateException If the value is not a collection.
     * @throws AssertionError If the value does not contain the expected element.
     */
    infix fun <R> anItemEqualTo(element: R): ShouldHave<T> {
        result.value.let {
            when (it) {
                is Collection<*> -> {
                    if (!it.contains(element)) {
                        throw AssertionError("Expected result to contain <$element> but it does not")
                    }
                }

                is Array<*> -> {
                    if (!it.contains(element)) {
                        throw AssertionError("Expected result to contain <$element> but it does not")
                    }
                }
                else -> throw IllegalStateException("Result value is not a collection")
            }
        }

        return this
    }

}
