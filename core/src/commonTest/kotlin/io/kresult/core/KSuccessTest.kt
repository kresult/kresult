package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import kotlin.test.Test

class KSuccessTest {

    @Test
    fun `success to string produces a readable result`() {
        "test".asSuccess().toString().shouldBeEqual("KSuccess(test)")
        false.asSuccess().toString().shouldBeEqual("KSuccess(false)")
        A("bar").asSuccess().toString().shouldBeEqual("KSuccess(A(foo=bar))")
    }

    data class A(val foo: String)
}