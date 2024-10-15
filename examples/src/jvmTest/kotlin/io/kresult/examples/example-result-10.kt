// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult10

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  KResult.Failure("test").swap() shouldBe KResult.Success("test")
  KResult.Success("test").swap() shouldBe KResult.Failure("test")
}
