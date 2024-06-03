package io.github.imagineDevit.giwt.kt.statements.functions

import io.github.imagineDevit.giwt.kt.TestCaseContext
import io.github.imagineDevit.giwt.kt.TestCaseResult

/**
 * Typealiases for the different types of context functions.
 */
typealias GCtxFn<T, R> = TestCaseContext.GCtx<T, R>.() -> T
typealias AGCtxFn<T, R> = TestCaseContext.AGCtx<T, R>.(T) -> Unit
typealias WCtxFFn<T, R> = suspend TestCaseContext.WCtx<T, R>.(T) -> R
typealias WCtxSFn<T, R> = suspend TestCaseContext.WCtx<T, R>.() -> R
typealias TCtxFn<T, R> = TestCaseContext.TCtx<T, R>.(TestCaseResult<R>) -> Unit
