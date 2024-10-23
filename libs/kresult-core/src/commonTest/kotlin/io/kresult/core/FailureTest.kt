package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class FailureTest {

  @Test
  fun `Failure_toString produces a readable result`() {
    "test".asFailure().toString().shouldBeEqual("KResult.Failure(test)")
    false.asFailure().toString().shouldBeEqual("KResult.Failure(false)")
    A("bar").asFailure().toString().shouldBeEqual("KResult.Failure(A(foo=bar))")
  }

  @Test
  fun `Failure_withValue produces FailureWithValue`() {
    KResult.Failure("err").withValue("foo") shouldBe KResult.FailureWithValue("err", "foo")
  }
  data class A(val foo: String)
}