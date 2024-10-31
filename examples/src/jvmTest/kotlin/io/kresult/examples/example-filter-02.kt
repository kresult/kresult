// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFilter02

import io.kresult.core.KResult
import io.kresult.core.filter
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success(42)
    .filter(
      failureValue = "Invalid value",
      f = { it > 0 }
    ) shouldBe KResult.Success(42)
}
