// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFilternull01

import io.kresult.core.KResult
import io.kresult.core.filterNull
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success("test")
    .filterNull { "Value was null" } shouldBe KResult.Success("test")

  KResult.Success(null)
    .filterNull { "Value was null" } shouldBe KResult.Failure("Value was null")
}
