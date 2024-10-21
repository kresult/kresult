// This file was automatically generated from README.md by Knit tool. Do not edit.
package io.kresult.examples.exampleReadme01

import io.kresult.core.KResult
import io.kresult.core.filter

fun test() {
  val greeting = KResult.Success("World")

  val res: KResult<String, String> = greeting
    // transform result, if successful
    .map {
      """Hello, $it!"""
    }
    // transform to IllegalArgumentException on failure side, if value is blank
    .filter(IllegalArgumentException("Greeting should not be blank")) {
      it.isNotBlank()
    }
    // execute side-effect, if successful
    .onSuccess {
      print("log: $it is successful")
    }
    // transform failure from Exception to a string
    .mapFailure {
      "${it.javaClass}: ${it.localizedMessage}"
    }

  // fold result to string
  res.fold(
    { error -> "Result failed with error: $error" },
    { value -> "Result was successful with value: $value" },
  )
}
