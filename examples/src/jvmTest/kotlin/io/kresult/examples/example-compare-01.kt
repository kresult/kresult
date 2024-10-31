// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleCompare01

import io.kresult.core.KResult
import io.kresult.core.compareTo
import io.kotest.matchers.shouldBe

fun test() {
  val success1 = KResult.Success(1)
  val success2 = KResult.Success(2)
  (success1 < success2) shouldBe true

  val failure1 = KResult.Failure("a")
  val failure2 = KResult.Failure("b")
  (failure1 < failure2) shouldBe true

  (failure1 < success1) shouldBe true  // Failures always less than successes
}
