// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult12

import io.kresult.core.KResult
import io.kotest.matchers.shouldBe

fun test() {
  val res: KResult<Nothing, Unit> =
    KResult.Success.unit

  res.isSuccess() shouldBe true
}
