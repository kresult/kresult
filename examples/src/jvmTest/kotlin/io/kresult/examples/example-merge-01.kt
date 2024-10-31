// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleMerge01

import io.kresult.core.KResult
import io.kresult.core.merge
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success("success").merge() shouldBe "success"
  KResult.Failure("failure").merge() shouldBe "failure"
  KResult.FailureWithValue("error", "value").merge() shouldBe "error"
}
