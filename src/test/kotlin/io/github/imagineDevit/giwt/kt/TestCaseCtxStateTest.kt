package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.annotations.Test
import io.github.imagineDevit.giwt.kt.expectations.equalTo
import io.github.imagineDevit.giwt.kt.expectations.notNull


class TestCaseCtxStateTest {
    @Test("create a TestCaseCtxState with a not null given value")
    fun of(tc: TestCase<Int, TestCaseCtxState<Int>>) {
        tc
            .given(" a not null number 1 ", 1)
            .`when`("a TestCaseCtxState is created using 'of' method factory") { TestCaseCtxState.of(it) }
            .then("the value of the TestCaseCtxState should be 1") {
                it shouldBe notNull() and equalTo(TestCaseCtxState.of(1))
            }
    }

    @Test("create a TestCaseCtxState with a null value")
    fun of2(tc: TestCase<Int?, TestCaseCtxState<Int?>>) {
        tc
            .given(" a not null number") { null }
            .`when`("a TestCaseCtxState is created using 'of' method factory") { TestCaseCtxState.of(it) }
            .then("the value of the TestCaseCtxState should be null") {
                it shouldBe notNull() and equalTo(TestCaseCtxState.of(null))
            }
    }

    @Test("create a TestCaseCtxState with no initial value")
    fun empty(tc: TestCase<Unit, TestCaseCtxState<Int?>>) {
        tc
            .`when`(
                "a TestCaseCtxState is created using 'empty' method factory"
            ) { TestCaseCtxState.empty() }
            .then("the value of the TestCaseCtxState should be null") { result ->
                result shouldBe notNull() and equalTo(TestCaseCtxState.of(null))
            }
    }
}