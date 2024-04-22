package io.github.imagineDevit.giwt.kt.assertions

import io.github.imagineDevit.giwt.core.utils.TextUtils
import java.util.function.Predicate

/**
 * Matching class that represents a predicate to test the value.
 * It contains a description of the predicate and the predicate itself.
 *
 * @param T The type of the value to test.
 */
data class Matching<T> internal constructor(val description: String = "", val predicate: Predicate<T>) {
    fun shouldTest(value: T) {
        if (!predicate.test(value)) {
            throw AssertionError(takeIf { description.isNotEmpty() }?.let {
                "Matching < ${TextUtils.yellow(description)} > failed "
            } ?: "Predicate failed")
        }
    }
}

/**
 * Creates a Matching object from a predicate.
 * The resulting Matching object will have an empty description.
 * @param predicate The matching predicate
 */
fun <T> predicate(predicate: Predicate<T>): Matching<T> = Matching(predicate = predicate)

/**
 * Creates a Matching object from a description and a predicate.
 * @param description The description of the predicate.
 * @param predicate The matching predicate.
 */
fun <T> matching(description: String, predicate: Predicate<T>): Matching<T> = Matching(description, predicate)

/**
 * Creates an array of Matching objects from a list of pairs.
 */
fun <T> matchings(vararg pairs: Pair<String, (T) -> Boolean>): Array<Matching<T>> =
    pairs.map { matching(it.first, it.second) }.toTypedArray()
