// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult15

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Failure("test").swap() shouldBe KResult.Success("test")
  KResult.Success("test").swap() shouldBe KResult.Failure("test")
}