// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult01

import io.kresult.core.KResult
import io.kresult.core.asKResult
import io.kresult.core.asSuccess
import io.kotest.matchers.shouldBe

fun test() {
  // by instance
  KResult.Success("test")
    .isSuccess() shouldBe true

  // by extension function
  "test".asSuccess()
    .isSuccess() shouldBe true

  // by catching exceptions
  KResult
    .catch {
      throw RuntimeException("throws")
    }
    .isSuccess() shouldBe false

  // from nullable
  KResult
    .fromNullable(null) {
      RuntimeException("Value can not be null")
    }
    .isSuccess() shouldBe false

  // from Kotlin Result
  Result.success("test")
    .asKResult()
    .isSuccess() shouldBe true
}
