// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult09

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Failure("test-failure")
    .failureOrNull() shouldBe "test-failure"

  KResult.Success("test-success")
    .failureOrNull() shouldBe null
}
