package io.github.imagineDevit.giwt.kt.statements.functions

import io.github.imagineDevit.giwt.kt.TestCaseResult

fun interface ThenFn<R> {
    fun apply(result: TestCaseResult<R>)
}