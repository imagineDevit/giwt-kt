package io.github.imagineDevit.giwt.kt.tests

import io.github.imagineDevit.giwt.core.callbacks.AfterAllCallback
import io.github.imagineDevit.giwt.core.callbacks.BeforeAllCallback

class MyTestExtension : BeforeAllCallback, AfterAllCallback {
    override fun beforeAll() {
        println("Before all")
    }

    override fun afterAll() {
        println("After all")
    }
}