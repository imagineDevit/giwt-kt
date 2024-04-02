package io.github.imagineDevit.giwt.kt.statements.functions

fun interface AndGivenFn<T> {
    fun apply(value: T): T
}
