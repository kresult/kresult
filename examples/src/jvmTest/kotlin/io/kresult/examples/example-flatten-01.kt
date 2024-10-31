// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFlatten01

import io.kresult.core.KResult
import io.kresult.core.flatten
import io.kotest.matchers.shouldBe

fun test() {
  val nested = KResult.Success(KResult.Success(42))
  nested.flatten() shouldBe KResult.Success(42)
}
