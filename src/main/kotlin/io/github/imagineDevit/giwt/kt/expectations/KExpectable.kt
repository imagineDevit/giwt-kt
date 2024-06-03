package io.github.imagineDevit.giwt.kt.expectations

import io.github.imagineDevit.giwt.core.expectations.*
import io.github.imagineDevit.giwt.core.expectations.Expectation.Should.ShouldFail
import io.github.imagineDevit.giwt.core.expectations.Expectation.Should.ShouldSucceed


interface KExpectable<T> : Expectable<T> {

    override infix fun shouldFail(expectation: ExpectedToFail): OnFailureChain<ExpectedToFail> =
        OnFailureChain(ShouldFail(this, listOf(expectation)).verifyAndGet())

    infix fun shouldFail(expectations: Array<ExpectedToFail>) = ShouldFail(this, expectations.asList()).verify()

    override infix fun shouldBe(expectation: ExpectedToBe<T>): OnValueChain<T, ExpectedToBe<T>> = verify(expectation)

    infix fun shouldBe(expectations: Array<ExpectedToBe<T>>) = verify(expectations)

    override infix fun shouldHave(expectation: ExpectedToHave<T>): OnValueChain<T, ExpectedToHave<T>> =
        verify(expectation)

    infix fun shouldHave(expectations: Array<ExpectedToHave<T>>) = verify(expectations)

    override infix fun shouldMatch(expectation: ExpectedToMatch<T>): OnValueChain<T, ExpectedToMatch<T>> =
        verify(expectation)

    infix fun shouldMatch(expectations: Array<ExpectedToMatch<T>>) = verify(expectations)

    private fun <E : Expectation.OnValue<T>> verify(expectation: E): OnValueChain<T, E> =
        OnValueChain(ShouldSucceed(this, listOf(expectation)).verifyAndGet(), this)

    private fun <E : Expectation.OnValue<T>> verify(expectations: Array<E>) =
        ShouldSucceed(this, expectations.asList()).verify()

}