package io.github.imagineDevit.giwt.kt

import io.github.imagineDevit.giwt.core.GiwtTestExecutor
import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.report.TestCaseReport

/**
 * The Kotlin implementation of the Giwt test executor.
 */
@Suppress("RAW_TYPES")
class KGiwtTestExecutor : GiwtTestExecutor<TestCase<*, *>>() {

    override fun run(testCase: TestCase<*, *>) = testCase.play()

    override fun createTestCase(
        name: String,
        report: TestCaseReport.TestReport?,
        parameter: TestParameters.Parameter?
    ): TestCase<*, *> = TestCase<Any, Any>(name, report, parameter)

}