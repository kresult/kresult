// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult04

import io.kresult.core.KResult
import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.Success
import io.kotest.matchers.shouldBe

fun test() {
  val res: KResult<String, Int> = Success(2)

  // when expression
  when (res) {
    is Success -> "success: ${res.value}"
    is Failure -> "failure: ${res.error}"
  } shouldBe "success: 2"

  // destructuring call
  val (failureValue, successValue) = res

  failureValue shouldBe null
  successValue shouldBe 2
}
