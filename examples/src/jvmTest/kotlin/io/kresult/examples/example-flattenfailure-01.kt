// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFlattenfailure01

import io.kresult.core.KResult
import io.kresult.core.flattenFailure
import io.kotest.matchers.shouldBe

fun test() {
  val error = KResult.Failure("inner error")
  val nested = KResult.Failure(error)
  nested.flattenFailure() shouldBe KResult.Failure("inner error")

  val success: KResult<KResult<Nothing, Int>, Int> = KResult.Success(42)
  success.flattenFailure() shouldBe KResult.Success(42)
}
