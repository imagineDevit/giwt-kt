package io.github.imagineDevit.giwt.kt.statements.functions

import io.github.imagineDevit.giwt.kt.TestCaseContext

/**
 * Typealiases for the different types of context functions.
 */
typealias GCtxFn<T, R> = TestCaseContext.GCtx<T, R>.() -> Unit
typealias AGCtxFn<T, R> = TestCaseContext.AGCtx<T, R>.() -> Unit
typealias WCtxFn<T, R> = suspend TestCaseContext.WCtx<T, R>.() -> Unit
typealias TCtxFn<T, R> = TestCaseContext.TCtx<T, R>.() -> Unit