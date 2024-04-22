package io.github.imagineDevit.giwt.kt.statements.functions

import java.util.function.Function
import java.util.function.Supplier


/**
 * Fun interface for the different types of when statements functions.
 */
sealed interface WhenFns {
    fun interface WhenFn<T, R> : WhenFns, Function<T, R>
    fun interface WhenSFn<R> : WhenFns, Supplier<R>
}
