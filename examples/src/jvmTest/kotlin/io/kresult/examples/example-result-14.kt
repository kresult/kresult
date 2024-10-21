// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult14

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success("test-success")
    .getOrNull() shouldBe "test-success"

  KResult.Failure("test-failure")
    .getOrNull() shouldBe null
}
