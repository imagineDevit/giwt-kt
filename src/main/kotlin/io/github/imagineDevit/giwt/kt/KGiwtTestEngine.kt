package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.GiwtTestEngine

/**
 * The Kotlin implementation of the Giwt test engine.
 */
class KGiwtTestEngine : GiwtTestEngine<TestCase<*, *>, KGiwtTestExecutor>(KGiwtTestExecutor())