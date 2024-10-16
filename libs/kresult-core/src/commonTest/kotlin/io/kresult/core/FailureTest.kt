package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import kotlin.test.Test

class FailureTest {

  @Test
  fun `failure to string produces a readable result`() {
    "test".asFailure().toString().shouldBeEqual("Failure(test)")
    false.asFailure().toString().shouldBeEqual("Failure(false)")
    A("bar").asFailure().toString().shouldBeEqual("Failure(A(foo=bar))")
  }

  data class A(val foo: String)
}