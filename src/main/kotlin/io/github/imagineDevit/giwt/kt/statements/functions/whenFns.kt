package io.github.imagineDevit.giwt.kt.statements.functions

sealed interface WhenFns {
    fun interface WhenFn<T, R> : WhenFns {
        fun apply(value: T): R
    }

    fun interface WhenSFn<R> : WhenFns {
        fun apply(): R
    }
}
