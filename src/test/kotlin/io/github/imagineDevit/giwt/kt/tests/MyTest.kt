package io.github.imagineDevit.giwt.kt.tests

import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.TestParameters.Parameter

import io.github.imagineDevit.giwt.core.annotations.*
import io.github.imagineDevit.giwt.kt.TestCase
import io.github.imagineDevit.giwt.kt.expectations.*
import kotlinx.coroutines.coroutineScope

@ExtendWith(MyTestExtension::class)
@ConfigureWith(MyTestConfiguration::class)
class MyTest {

    private suspend fun byTwoPlus(n: Int, m: Int): Int = coroutineScope { n * 2 + m }
    private suspend fun byTwoPlusOne(n: Int): Int = byTwoPlus(n, 1)

    @Test("(1 * 2) + 1 should be 3")
    fun test1(testCase: TestCase<Int, Int>) {
        testCase
            .given("state is 1", 1)
            .and("state is multiplied by 2") { it * 2 }
            .`when`("1 is added to the state") { byTwoPlusOne(it) }
            .then("result should be not null") {
                it shouldBe notNull() and equalTo(3)
            }
    }

    @Test("(1 * 2) + 1 should be null")
    fun test1b(testCase: TestCase<Int, Int?>) {
        testCase
            .given("state is 1", 1)
            .and("state is multiplied by 2") { it * 2 }
            .`when`("1 is added to the state") { null }
            .then("result should be not null") { it shouldBe `null`() }
    }

    @Test("(null * 2) + 1 should be null")
    fun test1c(testCase: TestCase<Int?, Int?>) {
        testCase.withContext()
            .given("state set at null", null)
            .`when`("1 is added to the state") { it?.let { byTwoPlusOne(it) } }
            .then("result should be not null") { it shouldBe `null`() }

    }

    @ParameterizedTest(
        name = "( 1 * 2 ) + {0} should be {1}",
        source = "getParams"
    )
    fun test2(testCase: TestCase<Int, Int>, number: Int, expected: Int) {
        testCase
            .given("state is 1") { 1 }
            .and("state is multiplied by 2") { it * 2 }
            .`when`("$number is added to the state") { byTwoPlus(it, number) }
            .then("result should be not null") { it shouldBe notNull() and equalTo(expected) }

    }

    @Test
    @Skipped(reason = "this test is skipped")
    fun test3(testCase: TestCase<Unit, Int?>) {
        testCase
            .given("nothing") {}
            .`when`("called method return 1") { 1 }
            .then("the result should be not null") { it shouldBe notNull() and equalTo(1) }
    }

    @Test("An illegalState exception should be thrown")
    fun test4(testCase: TestCase<Unit, Unit>) {
        testCase
            .`when`("called method throw an exception with oups message") { error("Oups") }
            .then("the exception is not null") {
                it shouldFail withType(IllegalStateException::class) and withMessage("Oups")
            }
    }

    @Test("test case with context")
    fun test5(testCase: TestCase<Int, Int>) {
        testCase.withContext()
            .given("the state is set to 1") { 1 }
            .and("the state is multiplied by 2") { setVar("one", it) }
            .`when`("result is set to state + 1") { it + 1 }
            .then("the result should be 3") { it shouldBe notNull() and equalTo(2) }
    }

    @Test("Add element to an empty collection")
    fun test6(testCase: TestCase<MutableList<String>, MutableList<String>>) {
        testCase.withContext()
            .given("an empty list", mutableListOf())
            .`when`("an element is added to the list") {
                it.add("element")
                it
            }
            .then("the result should be not null") { it shouldBe notNull() }
            .and("the result should have a size equal to 1") { it shouldHave size(1) }
            .and("the result should contain an item equal to 'element'") {
                it shouldMatch one("element") { list -> list.contains("element") }
            }
    }

    @Test("ctx An illegalState exception should be thrown")
    fun test7(testCase: TestCase<Unit, Unit>) {
        testCase
            .withContext()
            .`when`("called method throw an exception with oups message") { error("Oups") }
            .then("the exception is not null") {
                it shouldFail withType(IllegalStateException::class) and withMessage("Oups")
            }
    }

    companion object {
        @ParameterSource
        @JvmStatic
        fun getParams(): TestParameters<Parameter.P2<Int, Int>> = TestParameters.of(
            Parameter.P2.of(1, 3),
            Parameter.P2.of(2, 4),
            Parameter.P2.of(4, 6)
        )
    }
}