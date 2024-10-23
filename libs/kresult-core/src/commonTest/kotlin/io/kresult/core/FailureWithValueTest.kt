package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import kotlin.test.Test

class FailureWithValueTest {

  @Test
  fun `FailureWithValue_toString produces a readable result`() {
    "error".asFailure()
      .withValue("value")
      .toString().shouldBeEqual("KResult.FailureWithValue(error, value)")

    false.asFailure()
      .withValue("test")
      .toString().shouldBeEqual("KResult.FailureWithValue(false, test)")

    A("bar").asFailure()
      .withValue("value")
      .toString().shouldBeEqual("KResult.FailureWithValue(A(foo=bar), value)")
  }

  data class A(val foo: String)
}