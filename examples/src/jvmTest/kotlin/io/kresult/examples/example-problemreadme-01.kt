// This file was automatically generated from README.md by Knit tool. Do not edit.
package io.kresult.examples.exampleProblemreadme01

import io.kresult.core.KResult
import io.kresult.problem.Problem
import io.kotest.matchers.shouldBe

fun test() {
  val res = KResult.Failure(
    Problem.NotFound(detail = "User with that ID could not be found")
  )

  res.onFailure {
    println(it.toJson(pretty = true))
  }

  res.error.status shouldBe 404
}
