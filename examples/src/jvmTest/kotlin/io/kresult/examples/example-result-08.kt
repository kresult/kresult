// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult08

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  KResult.Failure(2)
    .mapFailure {
      it * it
    }
    .failureOrNull() shouldBe 4
}
