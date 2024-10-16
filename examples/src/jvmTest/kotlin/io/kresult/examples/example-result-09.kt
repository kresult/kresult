// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult09

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success(2)
    .map {
      it * it
    }
    .getOrNull() shouldBe 4
}
