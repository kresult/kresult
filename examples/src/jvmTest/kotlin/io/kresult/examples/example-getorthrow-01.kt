// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleGetorthrow01

import io.kresult.core.KResult
import io.kresult.core.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success(42)
    .getOrThrow() shouldBe 42

  val exception = RuntimeException("test error")
  shouldThrow<RuntimeException> {
    KResult.Failure(exception).getOrThrow()
  }.message shouldBe "test error"

  shouldThrow<RuntimeException> {
    KResult.FailureWithValue(exception, 42).getOrThrow()
  }.message shouldBe "test error"
}
