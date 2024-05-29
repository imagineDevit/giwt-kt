package io.github.imagineDevit.giwt.kt.expectations

import io.github.imagineDevit.giwt.core.expectations.Expectation
import io.github.imagineDevit.giwt.core.expectations.ExpectationChain
import io.github.imagineDevit.giwt.core.expectations.ExpectationChain.OnValue


data class OnFailureChain<E : Expectation.OnFailure>(val error: Throwable) : ExpectationChain.OnFailure<E>(error) {

    override infix fun and(expectation: E): OnFailureChain<E> {
        expectation.doVerify(error)
        return this
    }
}

data class OnValueChain<T, E : Expectation.OnValue<T>>(val value: T, val expectable: KExpectable<T>) : OnValue<T, E>(value) {

    override infix fun and(expectation: E): OnValueChain<T, E> {
        expectation.doVerify(value)
        return this
    }
}

