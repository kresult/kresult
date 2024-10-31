// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleCombine01

import io.kresult.core.KResult
import io.kresult.core.combine
import io.kotest.matchers.shouldBe

fun test() {
  // Combining successes
  val s1: KResult<String, Int> = KResult.Success(1)
  val s2: KResult<String, Int> = KResult.Success(2)
  s1.combine(
    s2,
    combineFailure = { e1, e2 -> "$e1 and $e2" },
    combineSuccess = { v1, v2 -> v1 + v2 }
  ) shouldBe KResult.Success(3)

  // Combining failures
  val f1: KResult<String, Int> = KResult.Failure("error1")
  val f2: KResult<String, Int> = KResult.Failure("error2")
  f1.combine(
    f2,
    combineFailure = { e1, e2 -> "$e1 and $e2" },
    combineSuccess = { v1, v2 -> v1 + v2 }
  ) shouldBe KResult.Failure("error1 and error2")

  // Mixing success and failure
  val mixed = s1.combine(
    f1,
    combineFailure = { e1, e2 -> "$e1 and $e2" },
    combineSuccess = { v1, v2 -> v1 + v2 }
  ) shouldBe KResult.Failure("error1")
}
