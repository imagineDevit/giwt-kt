package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.annotations.Test
import io.github.imagineDevit.giwt.kt.expectations.equalTo
import io.github.imagineDevit.giwt.kt.expectations.notNull
import io.github.imagineDevit.giwt.kt.expectations.one


class TestCaseCtxResultTest {

    @Test("create a TestCaseCtxResult with a given value")
    fun of(tc: TestCase<Int, TestCaseCtxResult<Int>>) {
        tc
            .given(" a not null number 1 ", 1)
            .`when`("a TestCaseCtxResult is created using 'of' method factory") { TestCaseCtxResult.of(it) }
            .then("the value of the TestCaseCtxResult should be 1") {
                it shouldBe notNull() and equalTo(TestCaseCtxResult.of(1))
            }
    }

    @Test("create a TestCaseCtxResult with a null value")
    fun of2(tc: TestCase<Int?, TestCaseCtxResult<Int?>>) {
        tc
            .given(" a null number") { null }
            .`when`("a TestCaseCtxResult is created using 'of' method factory") { TestCaseCtxResult.of(it) }
            .then("the value of the TestCaseCtxResult should be null") {
                it shouldBe notNull() and equalTo(TestCaseCtxResult.of(null))
            }
    }

    @Test("create a TestCaseCtxResult with a given exception")
    fun ofErr(tc: TestCase<Exception, TestCaseCtxResult<Exception>>) {
        tc.withContext()
            .given(" a not null exception ", Exception())
            .`when`("a TestCaseCtxResult is created using 'ofErr' method factory") {
                TestCaseCtxResult.ofErr(it)
            }
            .then("the value of the TestCaseCtxResult should be the exception") {
                it shouldBe notNull() and equalTo(TestCaseCtxResult.ofErr(getState().value()))
            }
    }


    @Test("create a empty TestCaseCtxResult")
    fun empty(tc: TestCase<Int?, TestCaseCtxResult<Int?>?>) {
        tc
            .`when`("a TestCaseCtxResult is created using 'empty' method factory") { TestCaseCtxResult.empty() }
            .then("the value of the TestCaseCtxResult should be null") {
                it shouldBe notNull() and equalTo(TestCaseCtxResult.of(null))
            }
    }

    @Test("get the result of a TestCaseCtxResult")
    fun result(tc: TestCase<TestCaseCtxResult<Int>, TestCaseResult<Int>>) {
        tc
            .given("a TestCaseCtxResult with a value") { TestCaseCtxResult.of(1) }
            .`when`("the result method is called") { it.result() }
            .then("the result should be a TestCaseResult with the same value") {
                it shouldBe notNull() and equalTo(TestCaseResult.of(1))
            }
    }

    @Test("get the result of a TestCaseCtxResult with an exception")
    fun result2(tc: TestCase<TestCaseCtxResult<Int>, TestCaseResult<Int>>) {
        tc
            .given("a TestCaseCtxResult with an exception") { TestCaseCtxResult.ofErr(Exception()) }
            .`when`("the result method is called") { it.result() }
            .then("the result should be a TestCaseResult with the same exception") {
                it shouldBe notNull()
                it shouldMatch one("Expected to have error of type Exception") { e -> e.resultError() is Exception }
            }
    }
}
