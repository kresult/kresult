package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import kotlin.test.Test

class SuccessTest {

  @Test
  fun `success to string produces a readable result`() {
    "test".asSuccess().toString().shouldBeEqual("Success(test)")
    false.asSuccess().toString().shouldBeEqual("Success(false)")
    A("bar").asSuccess().toString().shouldBeEqual("Success(A(foo=bar))")
  }

  data class A(val foo: String)
}