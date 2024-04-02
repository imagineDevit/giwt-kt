package io.github.imagineDevit.giwt.kt.statements.functions

import io.github.imagineDevit.giwt.kt.TestCaseContext
import io.github.imagineDevit.giwt.kt.TestCaseResult

fun interface ResCtxconsumer<T, R> {
    fun consume(ctx: TestCaseContext<T, R>, result: TestCaseResult<R>)
}