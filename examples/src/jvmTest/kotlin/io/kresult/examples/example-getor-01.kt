// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleGetor01

import io.kresult.core.KResult
import io.kresult.core.getOrDefault
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success(42)
    .getOrDefault { -1 } shouldBe 42

  KResult.Failure("error")
    .getOrDefault { -1 } shouldBe -1

  KResult.FailureWithValue("error", 42)
    .getOrDefault { -1 } shouldBe -1
}
