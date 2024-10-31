// This file was automatically generated from README.md by Knit tool. Do not edit.
package io.kresult.examples.exampleCore03

import io.kresult.core.KResult
import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.FailureWithValue
import io.kresult.core.KResult.Success
import io.kotest.matchers.shouldBe

fun test() {
  val res: KResult<String, Int> = Success(2)

  // when expression
  when (res) {
    is Success -> "success: ${res.value}"
    is Failure -> "failure: ${res.error}"
    is FailureWithValue -> "failure: ${res.error} (original value was ${res.value})"
  } shouldBe "success: 2"


  // destructuring call
  val (error, value) = res
  error shouldBe null
  value shouldBe 2
}
