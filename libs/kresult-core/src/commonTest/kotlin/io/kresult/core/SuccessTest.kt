package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class SuccessTest {

  @Test
  fun `KResult_Success_toString produces a readable result`() {
    "test".asSuccess().toString().shouldBeEqual("Success(test)")
    false.asSuccess().toString().shouldBeEqual("Success(false)")
    A("bar").asSuccess().toString().shouldBeEqual("Success(A(foo=bar))")
  }

  @Test
  fun `KResult_Success_unit can create success of Unit`() {
    KResult.Success.unit.shouldBeInstanceOf<KResult.Success<Unit>>()
  }

  data class A(val foo: String)
}