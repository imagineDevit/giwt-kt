package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.annotations.Test
import io.github.imagineDevit.giwt.core.errors.ResultValueError.*
import io.github.imagineDevit.giwt.kt.expectations.*


class TestCaseResultTest {


    @Test("create a TestCaseResult with a given value")
    fun of(tc: TestCase<Int, TestCaseResult<Int>>) {
        tc
            .given(" a not null number 1 ", 1)
            .`when`("a TestCaseResult is created using 'of' method factory") { TestCaseResult.of(it) }
            .then("the value of the TestCaseResult should be 1") {
                it shouldBe notNull() and equalTo(TestCaseResult.of(1))
            }
    }

    @Test("create a TestCaseResult with a null value")
    fun of2(tc: TestCase<Int?, TestCaseResult<Int?>>) {
        tc
            .given(" a null number") { null }
            .`when`("a TestCaseResult is created using 'of' method factory") { TestCaseResult.of(it) }
            .then("the value of the TestCaseResult should be null") {
                it shouldBe notNull() and equalTo(TestCaseResult.of(null))
            }
    }

    @Test("create a TestCaseResult with a given exception")
    fun ofErr(tc: TestCase<Exception, TestCaseResult<Exception>>) {
        tc.withContext()
            .given(" a not null exception ", Exception())
            .`when`("a TestCaseResult is created using 'ofErr' method factory") {
                TestCaseResult.ofErr(it)
            }
            .then("the value of the TestCaseResult should be the exception") {
                it shouldBe notNull() and equalTo(TestCaseResult.ofErr(getState().value()))
            }
    }


    @Test("create a empty TestCaseResult")
    fun empty(tc: TestCase<Int?, TestCaseResult<Int?>?>) {
        tc
            .`when`("a TestCaseResult is created using 'empty' method factory") { TestCaseResult.empty() }
            .then("the value of the TestCaseResult should be null") {
                it shouldBe notNull() and equalTo(TestCaseResult.of(null))
            }
    }

    @Test("map a TestCaseResult with a value")
    fun map(tc: TestCase<TestCaseResult<Int>, TestCaseResult<String>>) {
        tc
            .given(" a TestCaseResult with a value of 1 ", TestCaseResult.of(1))
            .`when`("the value of the TestCaseResult is mapped to a string") { it.map { v -> "$v" } }
            .then("the value of the TestCaseResult should be '1'") {
                it shouldBe notNull() and equalTo(TestCaseResult.of("1"))
            }
    }

    @Test("map a TestCaseResult with an exception")
    fun map2(tc: TestCase<TestCaseResult<Int>, TestCaseResult<String>>) {
        tc
            .given(" a TestCaseResult with a error ", TestCaseResult.ofErr(Exception()))
            .`when`("the value of the TestCaseResult is mapped to a string") { it.map { v -> "$v" } }
            .then("the TestCaseResult should fail") {
                it shouldFail withType(ExpectedValueFailed::class) and withMessage(EXPECTED_VALUE_FAILED)
            }
    }


    @Test("get the value of a TestCaseResult with a value")
    fun resultValue(tc: TestCase<TestCaseResult<Int>, Int>) {
        tc
            .given(" a TestCaseResult with a value of 1 ", TestCaseResult.of(1))
            .`when`("the value of the TestCaseResult is retrieved") { it.resultValue() }
            .then("the value should be 1") {
                it shouldBe notNull() and equalTo(1)
            }
    }

    @Test("try get the value of a TestCaseResult with an exception")
    fun resultValue2(tc: TestCase<TestCaseResult<Int>, Int>) {
        tc
            .given(" a TestCaseResult with an exception", TestCaseResult.ofErr(Exception()))
            .`when`("the value of the TestCaseResult is retrieved") { it.resultValue() }
            .then("the value should fail") {
                it shouldFail withType(ExpectedValueFailed::class) and withMessage(EXPECTED_VALUE_FAILED)
            }
    }


    @Test("get the error of a TestCaseResult with an exception")
    fun resultError(tc: TestCase<TestCaseResult<Int>, Throwable>) {
        tc
            .given(" a TestCaseResult with an exception", TestCaseResult.ofErr(Exception()))
            .`when`("the error of the TestCaseResult is retrieved") { it.resultError() }
            .then("the error should be not null") { it shouldBe notNull() }
            .and("the error should be an Exception") { it shouldMatch one("expected to instance of Exception") { e -> e is Exception } }
    }

    @Test("try get the error of a TestCaseResult with a value")
    fun resultError2(tc: TestCase<TestCaseResult<Int>, Throwable>) {
        tc
            .given(" a TestCaseResult with a value", TestCaseResult.of(1))
            .`when`("the value of the TestCaseResult is retrieved") { it.resultError() }
            .then("the value should fail") {
                it shouldFail withType(ExpectedErrorFailed::class) and withMessage(EXPECTED_ERROR_FAILED)
            }
    }
}
