// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult03

import io.kresult.core.KResult
import io.kresult.core.filter
import io.kresult.core.flatMap
import io.kresult.core.flatten
import io.kotest.matchers.shouldBe

fun test() {
  // map
  KResult.Success(3)
    .map { it - 1 }
    .getOrNull() shouldBe 2

  // flatMap
  KResult.Success("some-p4ss!")
    .flatMap {
      if (it.length > 8) {
        KResult.Success(it)
      } else {
        KResult.Failure(RuntimeException("Password is too short"))
      }
    } shouldBe KResult.Success("some-p4ss!")

  // filter
  KResult.Success("some-p4ss!")
    .filter(RuntimeException("String is empty")) {
      it.isNotBlank()
    } shouldBe KResult.Success("some-p4ss!")

  // flatten
  KResult.Success(KResult.Success(2))
    .flatten() shouldBe KResult.Success(2)
}
