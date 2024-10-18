// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult02

import io.kresult.integration.arrow.toKResult
import arrow.core.Either

fun test() {
  Either.Right("test")
    .toKResult()
}
