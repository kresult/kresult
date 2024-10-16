// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult01

import io.kresult.core.KResult
import io.kresult.core.asKResult
import io.kresult.core.asSuccess

fun test() {
  // by instance
  KResult.Success("test")

  // by extension function
  "test".asSuccess()

  // by catching exceptions
  KResult.catch {
    throw RuntimeException("throws")
  }

  // from nullable
  KResult.fromNullable(null) {
    RuntimeException("Value can not be null")
  }

  // from Kotlin Result
  Result.success("test")
    .asKResult()
}
