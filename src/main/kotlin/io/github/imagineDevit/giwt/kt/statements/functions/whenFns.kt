package io.github.imagineDevit.giwt.kt.statements.functions


/**
 * Fun interface for the different types of when statements functions.
 */
sealed interface WhenFns {
    fun interface WhenFn<T, R> : WhenFns, WFunction<T, R>
    fun interface WhenSFn<R> : WhenFns, WSupplier<R>
}

typealias WFunction<T, R> = suspend (T) -> R
typealias WSupplier<R> = suspend () -> R