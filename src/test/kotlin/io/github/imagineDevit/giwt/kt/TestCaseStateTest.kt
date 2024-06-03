package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.annotations.Test
import io.github.imagineDevit.giwt.kt.expectations.equalTo
import io.github.imagineDevit.giwt.kt.expectations.notNull


class TestCaseStateTest {
    @Test("create a TestCaseState with a not null given value")
    fun of(tc: TestCase<Int, TestCaseState<Int>>) {
        tc
            .given(" a not null number 1 ", 1)
            .`when`("a TestCaseState is created using 'of' method factory") { TestCaseState.of(it) }
            .then("the value of the TestCaseState should be 1") {
                it shouldBe notNull() and equalTo(TestCaseState.of(1))
            }
    }

    @Test("create a TestCaseState with a null value")
    fun of2(tc: TestCase<Int?, TestCaseState<Int?>>) {
        tc
            .given(" a not null number") { null }
            .`when`("a TestCaseState is created using 'of' method factory") { TestCaseState.of(it) }
            .then("the value of the TestCaseState should be null") {
                it shouldBe notNull() and equalTo(TestCaseState.of(null))
            }
    }

    @Test("create a TestCaseState with no initial value")
    fun empty(tc: TestCase<Unit, TestCaseState<Int?>>) {
        tc
            .`when`(
                "a TestCaseState is created using 'empty' method factory"
            ) { TestCaseState.empty() }
            .then("the value of the TestCaseState should be null") { result ->
                result shouldBe notNull() and equalTo(TestCaseState.of(null))
            }
    }
}