// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult01

import io.kresult.core.*
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Failure("test").isFailure() shouldBe true
  KResult.Success("test").isFailure() shouldBe false
}