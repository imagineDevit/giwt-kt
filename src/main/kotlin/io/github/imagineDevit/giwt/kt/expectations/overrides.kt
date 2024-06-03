package io.github.imagineDevit.giwt.kt.expectations

import io.github.imagineDevit.giwt.core.expectations.ExpectedToBe
import io.github.imagineDevit.giwt.core.expectations.ExpectedToFail
import io.github.imagineDevit.giwt.core.expectations.ExpectedToHave
import io.github.imagineDevit.giwt.core.expectations.ExpectedToMatch
import io.github.imagineDevit.giwt.core.expectations.ExpectedToMatch.Matching
import java.util.function.Predicate
import kotlin.reflect.KClass


// ToBe

fun <T> `null`(): ExpectedToBe<T> = ExpectedToBe.Null()

fun <T> notNull(): ExpectedToBe<T> = ExpectedToBe.NotNull()

fun <T> equalTo(expected: T): ExpectedToBe<T> = ExpectedToBe.EqualTo(expected)

fun <T> notEqualTo(expected: T): ExpectedToBe<T> = ExpectedToBe.NotEqualTo(expected)

fun <T> greaterThan(expected: T): ExpectedToBe<T> = ExpectedToBe.GreaterThan(expected)

fun <T> lessThan(expected: T): ExpectedToBe<T> = ExpectedToBe.LessThan(expected)

fun <T> between(min: T, max: T): ExpectedToBe<T> = ExpectedToBe.Between(min, max)

// ToHave

fun <T> size(value: Int): ExpectedToHave<T> = ExpectedToHave.Size(value)

fun <T, R> anItemEqualTo(element: R): ExpectedToHave<T> = ExpectedToHave.AnItemEqualTo(element)

// ToMatch

fun <T> one(description: String, predicate: Predicate<T>): ExpectedToMatch<T> =
    ExpectedToMatch.One(Matching(description, predicate))

fun <T> all(matchings: Map<String, Predicate<T>>): ExpectedToMatch<T> = ExpectedToMatch.All(matchings.map { Matching(it.key, it.value) })

fun <T> none(matchings: Map<String, Predicate<T>>): ExpectedToMatch<T> = ExpectedToMatch.None(matchings.map { Matching(it.key, it.value) })

// ToFail
fun <E : Throwable> withType(type: KClass<E>): ExpectedToFail.WihType = ExpectedToFail.WihType(type.java)

fun withMessage(message: String): ExpectedToFail = ExpectedToFail.WithMessage(message)