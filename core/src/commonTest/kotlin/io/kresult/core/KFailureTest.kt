package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import kotlin.test.Test

class KFailureTest {

    @Test
    fun `failure to string produces a readable result`() {
        "test".asFailure().toString().shouldBeEqual("KFailure(test)")
        false.asFailure().toString().shouldBeEqual("KFailure(false)")
        A("bar").asFailure().toString().shouldBeEqual("KFailure(A(foo=bar))")
    }

    data class A(val foo: String)
}