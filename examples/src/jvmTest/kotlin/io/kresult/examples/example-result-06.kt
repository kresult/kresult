// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult06

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  var result = ""

  KResult.Success("test-success")
    .onSuccess {
      result += it
    }

  result shouldBe "test-success"
}
