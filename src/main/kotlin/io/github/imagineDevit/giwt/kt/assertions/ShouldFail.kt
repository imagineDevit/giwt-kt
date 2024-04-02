package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.ATestCaseResult
import kotlin.reflect.KClass

data class ShouldFail(val result: ATestCaseResult.ResultValue.Err<*>) {

    infix fun withErrorOfType(errorClass: KClass<out Exception>): ShouldFail {
        if (!errorClass.isInstance(result.error)) {
            throw AssertionError("Expected error to be of type <" + errorClass.simpleName + "> but got <" + result.error.javaClass.name + ">")
        }
        return this
    }

    infix fun withMessage(message: String): ShouldFail {
        if (result.error.message != message) {
            throw AssertionError("Expected error message to be <" + message + "> but got <" + result.error.message + ">")
        }
        return this
    }
}
