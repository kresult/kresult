// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult08

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success("test").isSuccess() shouldBe true
  KResult.Failure("test").isSuccess() shouldBe false
}
