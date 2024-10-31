// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFlatmap01

import io.kresult.core.KResult
import io.kresult.core.flatMap
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success(5)
    .flatMap { value ->
      if (value > 0) KResult.Success(value * 2)
      else KResult.Failure("Value must be positive")
    } shouldBe KResult.Success(10)
}
