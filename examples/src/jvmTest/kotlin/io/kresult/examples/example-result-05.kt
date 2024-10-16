// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult05

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  KResult.Success("test").isSuccess() shouldBe true
  KResult.Failure("test").isSuccess() shouldBe false
}
