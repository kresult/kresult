// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult04

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  KResult.Success(2)
    .map {
      it * it
    }
    .getOrNull() shouldBe 4
}
