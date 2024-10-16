// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult03

import io.kresult.core.KResult
import io.kresult.core.filter
import io.kresult.core.flatMap
import io.kresult.core.flatten

fun test() {
  // map
  KResult.Success(2)
    .map { it - 1 }

  // flatMap
  KResult.Success("some-p4ss!").flatMap {
    if (it.length > 8) {
      KResult.Success(it)
    } else {
      KResult.Failure(RuntimeException("Password is too short"))
    }
  }

  // filter
  KResult.Success("some-p4ss!").filter(
    { it.isNotBlank() },
    { RuntimeException("String is empty") }
  )

  // flatten
  val nested: KResult<Throwable, KResult<Throwable, Int>> =
    KResult.Success(KResult.Success(2))

  val flattened: KResult<Throwable, Int> =
    nested.flatten()
}
