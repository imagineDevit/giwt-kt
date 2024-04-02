package io.github.imagineDevit.giwt.kt.tests


import io.github.imagineDevit.giwt.core.TestConfiguration
import io.github.imagineDevit.giwt.core.TestParameters
import io.github.imagineDevit.giwt.core.annotations.ParameterSource

class MyTestConfiguration : TestConfiguration() {

    @ParameterSource("getParams")
    fun parameters(): TestParameters<TestParameters.Parameter.P2<Int, Int>> {
        return TestParameters.of(
            TestParameters.Parameter.P2.of(1, 3),
            TestParameters.Parameter.P2.of(2, 4)
        )
    }
}
