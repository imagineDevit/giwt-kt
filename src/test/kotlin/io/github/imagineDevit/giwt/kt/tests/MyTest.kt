package io.github.imagineDevit.giwt.kt.tests

import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.TestParameters.Parameter

import io.github.imagineDevit.giwt.core.annotations.*
import io.github.imagineDevit.giwt.kt.TestCase
import io.github.imagineDevit.giwt.kt.expectations.*
import kotlinx.coroutines.coroutineScope
import java.lang.annotation.Inherited


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
                result shouldBe notNull() and equalTo(3)
            }

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
            .then("result should be not null") {
                result shouldBe notNull() and equalTo(expected)
            }

    }

    @Test
    @Skipped(reason = "this test is skipped")
    fun test3(testCase: TestCase<Unit, Int?>) {
        testCase
            .given("nothing") {}
            .`when`("called method return 1") { 1 }
            .then("the result should be not null") {
                result shouldBe notNull() and equalTo(1)
            }
    }

    @Test("An illegalState exception should be thrown")
    fun test4(testCase: TestCase<Unit, Unit>) {
        testCase
            .`when`("called method throw an exception with oups message") {
                error("Oups")
            }
            .then("the exception is not null") {
                result shouldFail withType(IllegalStateException::class) and withMessage("Oups")
            }
    }

    @Test("test case with context")
    fun test5(testCase: TestCase<Int, Int>) {
        testCase.withContext()
            .given("the state is set to 1") { setState(1) }
            .and("the state is multiplied by 2") {
                applyOnState {
                    setVar("one", it)
                }
            }
            .`when`("result is set to state + 1") { mapToResult { one -> one + 1 } }
            .then("the result should be 3") {
                result shouldBe notNull() and  equalTo(2)
            }
    }

    @Test("Add element to an empty collection")
    fun test6(testCase: TestCase<MutableList<String>, MutableList<String>>) {
        testCase.withContext()
            .given("an empty list", mutableListOf())
            .`when`("an element is added to the list") {
                applyOnState { list -> list.add("element") }
                setStateAsResult()
            }
            .then("the result should be not null") { result shouldBe notNull() }
            .and("the result should have a size equal to 1") { result  shouldHave size(1)  }
            .and("the result should contain an item equal to 'element'") {
                result shouldMatch one( matching("element") { it.contains( "element") })
            }
    }

    @Test("ctx An illegalState exception should be thrown")
    fun test7(testCase: TestCase<Void?, Void?>) {
        testCase
            .withContext()
            .`when`("called method throw an exception with oups message") { error("Oups") }
            .then("the exception is not null") {
                result shouldFail withType(IllegalStateException::class) and withMessage("Oups")
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