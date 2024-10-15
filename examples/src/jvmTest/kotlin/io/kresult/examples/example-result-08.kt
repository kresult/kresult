// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult08

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  KResult.Success("test-success")
    .getOrNull() shouldBe "test-success"

  KResult.Failure("test-failure")
    .getOrNull() shouldBe null
}
