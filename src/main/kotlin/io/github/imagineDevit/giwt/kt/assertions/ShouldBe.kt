package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult
import io.github.imagineDevit.giwt.core.utils.Utils

/**
 * ShouldBe class that provides a set of methods to assert the value of a Result.Ok.
 * @param T The type of the value contained in the Result.
 *
 * @see ATestCaseResult.ResultValue
 */
@Suppress("unused")
data class ShouldBe<T>(val result: ATestCaseResult.ResultValue.Ok<T>) {

    /**
     * Asserts that the value is null.
     *
     * @throws AssertionError If the value is not null.
     */
    val null_ get() = result.value?.let {} ?: throw AssertionError("Expected null value but got " + result.value)

    /**
     * Asserts that the value is not null.
     *
     * @throws AssertionError If the value is null.
     */
    val notNull: ShouldBe<T>
        get() = result.value?.let { this } ?: throw AssertionError("Expected not null value but got <null>")

    /**
     * Asserts that the value is equal to the expected value.
     *
     * @param expected The expected value.
     * @throws AssertionError If the value is not equal to the expected value.
     */
    infix fun equalTo(expected: T): ShouldBe<T> =
        if (result.value != expected) {
            throw AssertionError("Expected value to be <" + expected + "> but got <" + result.value + ">")
        } else this


    /**
     * Asserts that the value is different from the expected value.
     *
     * @param expected The expected value.
     * @throws AssertionError If the value is equal to the expected value.
     */
    infix fun notEqualTo(expected: T): ShouldBe<T> =
        if (result.value == expected) {
            throw AssertionError("Expected value to be different from <" + expected + "> but got <" + result.value + ">")
        } else this


    /**
     * Asserts that the value is between two expected values.
     *
     * @param min The minimum expected value.
     * @param max The maximum expected value.
     *
     * @throws IllegalStateException If the value is not comparable.
     * @throws AssertionError If the value is not between the expected values.
     */
    fun between(min: T, max: T): ShouldBe<T> {
        val c = Utils.asComparableOrThrow(
            result.value
        ) { IllegalStateException("Value is not comparable") }

        if (c < min || c > max) {
            throw AssertionError("Expected value to be between <$min> and <$max> but got <$c>")
        }

        return this
    }

    /**
     * Asserts that the value is greater than the expected value.
     *
     * @param value The minimum expected value.
     * @throws IllegalStateException If the value is not comparable.
     * @throws AssertionError If the value is not greater than the expected value.
     */
    infix fun greaterThan(value: T): ShouldBe<T> {
        val c = Utils.asComparableOrThrow(
            result.value
        ) { IllegalStateException("Value is not comparable") }
        if (c <= value) {
            throw AssertionError("Expected value to be greater than <$value> but got <$c>")
        }

        return this
    }

    /**
     * Asserts that the value is lesser than the expected value.
     *
     * @param value The maximum expected value.
     * @throws IllegalStateException If the value is not comparable.
     * @throws AssertionError If the value is not lesser than the expected value.
     */
    infix fun lesserThan(value: T): ShouldBe<T> {
        val c = Utils.asComparableOrThrow(
            result.value
        ) { IllegalStateException("Value is not comparable") }
        if (c >= value) {
            throw AssertionError("Expected value to be lesser than <$value> but got <$c>")
        }

        return this
    }

    /**
     * Asserts that the value is one of a given list of values.
     *
     * @param values the list of value.
     * @throws AssertionError If the value is not one of the expected values.
     */
    infix fun oneOf(values: Collection<T>): ShouldBe<T> {
        if (!values.contains(result.value)) {
            throw AssertionError("Expected value to be one of <" + values + "> but got <" + result.value + ">")
        }

        return this
    }
}
