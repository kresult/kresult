// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult09

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe
import kotlin.test.fail

fun test() {
  KResult.Success(1)
    .fold(
      { fail("Cannot be left") },
      { it + 1 }
    ) shouldBe 2
}
