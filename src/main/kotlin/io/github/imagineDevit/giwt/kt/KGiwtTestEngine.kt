package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.GiwtTestEngine


class KGiwtTestEngine : GiwtTestEngine<TestCase<*, *>, KGiwtTestExecutor>(KGiwtTestExecutor())