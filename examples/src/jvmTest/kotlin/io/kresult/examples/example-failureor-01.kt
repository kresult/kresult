// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFailureor01

import io.kresult.core.KResult
import io.kresult.core.failureOrDefault
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Failure("error")
    .failureOrDefault { "no error" } shouldBe "error"

  KResult.Success(42)
    .failureOrDefault { "no error" } shouldBe "no error"

  KResult.FailureWithValue("error", 42)
    .failureOrDefault { "no error" } shouldBe "error"
}
