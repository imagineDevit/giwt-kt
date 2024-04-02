package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult
import io.github.imagineDevit.giwt.core.utils.Utils

@Suppress("unused")
data class ShouldBe<T>(val result: ATestCaseResult.ResultValue.Ok<T>) {

    val null_ get() = result.value?.let {} ?: throw AssertionError("Expected null value but got " + result.value)

    val notNull: ShouldBe<T>
        get() = result.value?.let { this } ?: throw AssertionError("Expected not null value but got <null>")

    infix fun equalTo(expected: T): ShouldBe<T> =
        if (result.value != expected) {
            throw AssertionError("Expected value to be <" + expected + "> but got <" + result.value + ">")
        } else this


    infix fun notEqualTo(expected: T): ShouldBe<T> =
        if (result.value == expected) {
            throw AssertionError("Expected value to be different from <" + expected + "> but got <" + result.value + ">")
        } else this


    fun between(min: T, max: T): ShouldBe<T> {
        val c = Utils.asComparableOrThrow(
            result.value
        ) { AssertionError("Value is not comparable") }

        if (c < min || c > max) {
            throw AssertionError("Expected value to be between <$min> and <$max> but got <$c>")
        }

        return this
    }

    infix fun greaterThan(min: T): ShouldBe<T> {
        val c = Utils.asComparableOrThrow(
            result.value
        ) { AssertionError("Value is not comparable") }
        if (c <= min) {
            throw AssertionError("Expected value to be greater than <$min> but got <$c>")
        }

        return this
    }

    infix fun lesserThan(max: T): ShouldBe<T> {
        val c = Utils.asComparableOrThrow(
            result.value
        ) { AssertionError("Value is not comparable") }
        if (c >= max) {
            throw AssertionError("Expected value to be lesser than <$max> but got <$c>")
        }

        return this
    }

    infix fun oneOf(values: Collection<T>): ShouldBe<T> {
        if (!values.contains(result.value)) {
            throw AssertionError("Expected value to be one of <" + values + "> but got <" + result.value + ">")
        }

        return this
    }
}
