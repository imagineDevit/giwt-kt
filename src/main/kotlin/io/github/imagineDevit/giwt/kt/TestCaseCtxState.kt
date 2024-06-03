package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.ATestCaseState
import java.util.*

/**
 * The state of a [TestCaseWithContext].
 * @param T The type of the state value
 */
class TestCaseCtxState<T> private constructor(value: T?) : ATestCaseState<T>(value) {

    companion object {
        /**
         * Creates a new [TestCaseCtxState] with the given value.
         * @param value The value of the state.
         */
        fun <T> of(value: T): TestCaseCtxState<T> {
            return TestCaseCtxState(value)
        }

        /**
         * Creates a new empty [TestCaseCtxState].
         */
        fun <T> empty(): TestCaseCtxState<T> {
            return TestCaseCtxState(null)
        }
    }

    /**
     * Gets the value of the state.
     */
    fun value(): T {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return Objects.equals(value, (other as TestCaseCtxState<*>).value)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(value)
    }
}