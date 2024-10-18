// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult06

import io.kresult.core.KResult
import io.kresult.core.MultiErrorKResult
import io.kresult.core.errors
import io.kresult.core.validate
import io.kotest.matchers.shouldBe

data class User(val name: String, val age: Int, val active: Boolean)

enum class ValidationError(val message: String) {
  InvalidName("Name must not be empty"),
  IllegalAge("User must be over 18 years old"),
  Inactive("User must active"),
}

fun test() {
  val user: MultiErrorKResult<ValidationError, User> =
    KResult.Success(User(name = "Max", age = 17, active = false))

  val result: MultiErrorKResult<ValidationError, User> = user
    .validate(ValidationError.InvalidName) { u ->
      u.name.isNotBlank()
    }
    .validate(ValidationError.IllegalAge) { u ->
      u.age >= 18
    }
    .validate(ValidationError.Inactive) { u ->
      u.active
    }

  result.isFailure() shouldBe true
  result.errors().size shouldBe 2
  result.errors()[0] shouldBe ValidationError.IllegalAge
  result.errors()[1] shouldBe ValidationError.Inactive
}
