package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult
import kotlin.reflect.KClass

/**
 * ShouldFail class that provides a set of methods to assert the error of a Result.Err.
 *
 * @see ATestCaseResult.ResultValue
 */
data class ShouldFail(val result: ATestCaseResult.ResultValue.Err<*>) {

    /**
     * Asserts that the error is of the expected type.
     *
     * @param errorClass The expected type of the error.
     * @throws AssertionError If the error is not of the expected type.
     */
    infix fun withErrorOfType(errorClass: KClass<out Exception>): ShouldFail {
        if (!errorClass.isInstance(result.error)) {
            throw AssertionError("Expected error to be of type <" + errorClass.simpleName + "> but got <" + result.error.javaClass.name + ">")
        }
        return this
    }

    /**
     * Asserts that the error message is equal to the expected message.
     *
     * @param message The expected error message.
     * @throws AssertionError If the error message is not equal to the expected message.
     */
    infix fun withMessage(message: String): ShouldFail {
        if (result.error.message != message) {
            throw AssertionError("Expected error message to be <" + message + "> but got <" + result.error.message + ">")
        }
        return this
    }
}
