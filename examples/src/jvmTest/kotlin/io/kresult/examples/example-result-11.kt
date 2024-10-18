// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult11

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Failure(2)
    .mapFailure {
      it * it
    }
    .failureOrNull() shouldBe 4
}
