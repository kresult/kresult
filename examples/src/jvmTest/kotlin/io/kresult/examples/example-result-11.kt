// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult11

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult

fun test() {
  val res: KResult<Nothing, Unit> =
    KResult.Success.unit

  res.isSuccess() shouldBe true
}
