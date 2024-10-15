// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult06

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  var result = ""

  KResult.Success("test-success")
    .onSuccess {
      result += it
    }

  result shouldBe "test-success"
}
