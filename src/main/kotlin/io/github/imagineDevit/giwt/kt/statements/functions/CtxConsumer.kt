package io.github.imagineDevit.giwt.kt.statements.functions

import io.github.imagineDevit.giwt.kt.TestCaseContext

fun interface CtxConsumer<R, C : TestCaseContext<*, R>> {

    fun consume(ctx: C)
}