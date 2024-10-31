// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFilter01

import io.kresult.core.KResult
import io.kresult.core.filter
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success(42)
    .filter(
      failureFn = { value -> "Value $value is not positive" },
      f = { it > 0 }
    ) shouldBe KResult.Success(42)

  KResult.Success(-1)
    .filter(
      failureFn = { value -> "Value $value is not positive" },
      f = { it > 0 }
    ) shouldBe KResult.Failure("Value -1 is not positive")
}
