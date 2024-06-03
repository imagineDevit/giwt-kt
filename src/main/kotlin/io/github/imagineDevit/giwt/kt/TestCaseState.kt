package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseState
import java.util.*

/**
 * The state of a test case.
 * @param T The type of the state value.
 */
@Suppress("UNUSED")
open class TestCaseState<T> protected constructor(value: T?) : ATestCaseState<T>(value) {

    companion object {
        /**
         * Creates a new test case state with the given value.
         */
        fun <T> of(value: T): TestCaseState<T> = TestCaseState(value)

        /**
         * Create a new test case state with a null value
         */
        fun <T> empty(): TestCaseState<T> = TestCaseState(null)
    }

    /**
     * Apply a function to the value of the test case state.
     * @param fn The function to apply.
     */
    internal fun consumeValue(fn: (T) -> Unit) {
        super.consumeValue(fn)
    }

    internal fun value(): T {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return Objects.equals(value, (other as TestCaseState<*>).value)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(value)
    }

}