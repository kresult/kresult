// This file was automatically generated from README.md by Knit tool. Do not edit.
package io.kresult.examples.exampleReadme02

import io.kresult.core.*
import io.kotest.matchers.shouldBe

// Product to encode success
data class Greeting(val name: String)

// SUm to encode a failure
sealed class Failure(val msg: String) {

  // Indicates that Failure was caused by invalid input
  interface InputValidationProblem
  data object BlankName : Failure("Name should not be blank"), InputValidationProblem
  data object IllegalCharacters : Failure("Name contains illegal characters"), InputValidationProblem

  data object Unknown : Failure("An unknown error occured")
}

// Product to encode a response
data class Response(val status: Int, val content: String)

fun test() {
  val greeting: KResult<Failure, Greeting> = Greeting("World")
    // Wraps any type into a KResult.Success
    .asSuccess()
    // Filter, transforming to failure if greeting name is blank
    .filter(Failure.BlankName) {
      it.name.isNotBlank()
    }
    .filter(Failure.IllegalCharacters) {
      !it.name.contains("/")
    }

  val response: Response = greeting
    // Transforms Success side to a Response
    .map {
      Response(status = 200, content = """Hello, ${it.name}!""")
    }
    // Transforms Failure side to a Response
    .mapFailure { failure ->
      when (failure) {
        is Failure.InputValidationProblem ->
          Response(status = 400, content = failure.msg)

        else ->
          Response(status = 500, content = failure.msg)
      }
    }
    // When Success and Failure side are of the same type, we can merge them (long syntax for fold)
    .merge()

  response.status shouldBe 200
  response.content shouldBe "Hello, World!"
}
