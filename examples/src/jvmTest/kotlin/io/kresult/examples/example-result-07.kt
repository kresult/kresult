// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult07

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  var result = ""

  KResult.Failure("test-failure")
    .onFailure {
      result += it
    }

  result shouldBe "test-failure"
}
