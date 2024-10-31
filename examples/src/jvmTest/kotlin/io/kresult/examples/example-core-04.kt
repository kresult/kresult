// This file was automatically generated from README.md by Knit tool. Do not edit.
package io.kresult.examples.exampleCore04

import io.kresult.core.KResult
import io.kresult.core.getOrDefault
import io.kresult.core.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

fun test() {
  // fold
  KResult.Success(2).fold(
    { "failure: $it" },
    { "success: $it" }
  ) shouldBe "success: 2"

  // getOrNull
  KResult.Success(2).getOrNull() shouldBe 2
  KResult.Failure("error").getOrNull() shouldBe null

  // getOrElse
  KResult.Success(2).getOrDefault { -1 } shouldBe 2
  KResult.Failure("error").getOrDefault { -1 } shouldBe -1

  // getOrThrow
  KResult.Success(2).getOrThrow() shouldBe 2
  shouldThrow<RuntimeException> {
    KResult.Failure(RuntimeException("test")).getOrThrow()
  }
}
