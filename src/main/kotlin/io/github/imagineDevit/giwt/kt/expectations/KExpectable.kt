package io.github.imagineDevit.giwt.kt.expectations

import io.github.imagineDevit.giwt.core.expectations.*


interface KExpectable<T> : Expectable<T> {

    override infix fun shouldFail(expectation: ExpectedToFail): OnFailureChain<ExpectedToFail> {
        val resultError = resultError()
        expectation.doVerify(resultError)
        return OnFailureChain(resultError)
    }

    infix fun shouldFail(expectations: Array<ExpectedToFail>) {
        val resultError = resultError()
        expectations.forEach { it.doVerify(resultError) }
    }

    override infix fun shouldBe(expectation: ExpectedToBe<T>): OnValueChain<T, ExpectedToBe<T>> = verify(expectation)

    infix fun shouldBe(expectations: Array<ExpectedToBe<T>>) {
        verify(expectations)
    }

    override infix fun shouldHave(expectation: ExpectedToHave<T>): OnValueChain<T, ExpectedToHave<T>> =
        verify(expectation)

    infix fun shouldHave(expectations: Array<ExpectedToHave<T>>) {
        verify(expectations)
    }

    override infix fun shouldMatch(expectation: ExpectedToMatch<T>): OnValueChain<T, ExpectedToMatch<T>> =
        verify(expectation)

    infix fun shouldMatch(expectations: Array<ExpectedToMatch<T>>) {
        verify(expectations)
    }

    val result
        get() = this

    private fun <E : Expectation.OnValue<T>> verify(expectation: E): OnValueChain<T, E> {
        val resultValue = resultValue()
        expectation.doVerify(resultValue)
        return OnValueChain(resultValue, this)
    }

    private fun <E : Expectation.OnValue<T>> verify(expectations: Array<E>) {
        val resultValue = resultValue()
        expectations.forEach { it.doVerify(resultValue) }
    }
}