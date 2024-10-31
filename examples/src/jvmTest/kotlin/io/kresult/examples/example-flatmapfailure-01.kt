// This file was automatically generated from KResultExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleFlatmapfailure01

import io.kresult.core.KResult
import io.kresult.core.flatMapFailure
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Failure("error")
    .flatMapFailure { err ->
      if (err.length > 3) KResult.Success("recovered")
      else KResult.Failure("unrecoverable")
    } shouldBe KResult.Success("recovered")
}
