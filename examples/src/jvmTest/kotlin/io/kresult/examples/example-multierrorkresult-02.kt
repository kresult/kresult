// This file was automatically generated from MultiErrorKResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleMultierrorkresult02

import io.kresult.core.KResult
import io.kresult.core.MultiErrorKResult
import io.kresult.core.flatMap
import io.kresult.core.validate
import io.kotest.matchers.shouldBe

fun test() {
  val num: MultiErrorKResult<String, Int> = KResult.Success(7)

  val result = num
    .validate("Must be greater zero") {
      it > 0
    }
    .flatMap {
      // while this compiles...
      if (it % 3 == 0) {
        KResult.Success(it)
      } else {
        KResult.Failure(listOf("Must be modulus of 3"))
      }
    }
    .validate("Must be even") {
      // this will never be called, if the flatMap before evaluates to Failure
      it % 2 == 0
    }

  // we would expect a second error: "Must be even" here, but the last validation was never called
  result shouldBe KResult.Failure(listOf("Must be modulus of 3"))
}
