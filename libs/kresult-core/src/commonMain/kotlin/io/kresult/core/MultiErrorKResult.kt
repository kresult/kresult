package io.kresult.core

import io.kresult.core.KResult.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Typealias to represent [KResult] types that have a [List] type on the [Failure] side
 *
 * <!--- TEST_NAME MultiErrorKResultKnitTest -->
 *
 * ## Validation
 *
 * [MultiErrorKResult] instances have [validate] and [errors] extension functions. They can be used for very easy,
 * type-safe and side-effect-free object validation:
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.MultiErrorKResult
 * import io.kresult.core.errors
 * import io.kresult.core.validate
 * import io.kotest.matchers.shouldBe
 *
 * data class User(val name: String, val age: Int, val active: Boolean)
 *
 * enum class ValidationError(val message: String) {
 *   InvalidName("Name must not be empty"),
 *   IllegalAge("User must be over 18 years old"),
 *   Inactive("User must active"),
 * }
 *
 * fun test() {
 *   val user: MultiErrorKResult<ValidationError, User> =
 *     KResult.Success(User(name = "Max", age = 17, active = false))
 *
 *   val result: MultiErrorKResult<ValidationError, User> = user
 *     .validate(ValidationError.InvalidName) { u ->
 *       u.name.isNotBlank()
 *     }
 *     .validate(ValidationError.IllegalAge) { u ->
 *       u.age >= 18
 *     }
 *     .validate(ValidationError.Inactive) { u ->
 *       u.active
 *     }
 *
 *   result.isFailure() shouldBe true
 *   result.errors().size shouldBe 2
 *   result.errors()[0] shouldBe ValidationError.IllegalAge
 *   result.errors()[1] shouldBe ValidationError.Inactive
 * }
 * ```
 * <!--- KNIT example-multierrorkresult-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @since 0.2.0
 * @see MultiValueKResult if success side carries a list
 */
typealias MultiErrorKResult<E, T> = KResult<List<E>, T>

/**
 * Validates a [Success] or a [FailureWithValue] against a predicate ([expectationFn]] and applies [failureValue] if
 * not fulfilled
 *
 * This will return a [FailureWithValue] in case of an unmatched predicate, so multiple validation failures can be
 * tracked without early termination. If you prefer early termination on the first error, see [KResult.filter] instead.
 *
 * ## flatMap-ing validation results
 *
 * If you need to [flatMap] a validation result (which is either a [Success] or a [FailureWithValue]), make sure that
 * there are **no successive [validate] calls**. In the failure case of a [flatMap] operation, we lose the type
 * information of [FailureWithValue.value] and therefore need to return a plain [Failure] object. This, will cause all
 * successive [validate] calls to be ignored without being evaluated, as we have nothing to apply the predicate function
 * to. While such scenarios compile, having validations that are never applied should be, at least semantically,
 * considered bugs.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.MultiErrorKResult
 * import io.kresult.core.flatMap
 * import io.kresult.core.validate
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   val num: MultiErrorKResult<String, Int> = KResult.Success(7)
 *
 *   val result = num
 *     .validate("Must be greater zero") {
 *       it > 0
 *     }
 *     .flatMap {
 *       // while this compiles...
 *       if (it % 3 == 0) {
 *         KResult.Success(it)
 *       } else {
 *         KResult.Failure(listOf("Must be modulus of 3"))
 *       }
 *     }
 *     .validate("Must be even") {
 *       // this will never be called, if the flatMap before evaluates to Failure
 *       it % 2 == 0
 *     }
 *
 *   // we would expect a second error: "Must be even" here, but the last validation was never called
 *   result shouldBe KResult.Failure(listOf("Must be modulus of 3"))
 * }
 * ```
 * <!--- KNIT example-multierrorkresult-02.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @since 0.2.0
 * @see MultiErrorKResult for examples
 */
@OptIn(ExperimentalContracts::class)
inline fun <E, T> MultiErrorKResult<E, T>.validate(
  failureValue: E,
  expectationFn: (success: T) -> Boolean
): MultiErrorKResult<E, T> {
  contract {
    callsInPlace(expectationFn, InvocationKind.AT_MOST_ONCE)
  }

  return when (this) {
    is Failure ->
      this

    is Success ->
      if (expectationFn(value))
        this
      else
        FailureWithValue(listOf(failureValue), value)

    is FailureWithValue ->
      if (expectationFn(value))
        this
      else
        FailureWithValue(error + failureValue, value)
  }
}

/**
 * Returns a list of errors
 *
 * @return list of errors, if the result is a [Failure] or an empty list, if result is a [Success]
 */
fun <E, T> MultiErrorKResult<E, T>.errors(): List<E> =
  failureOrDefault { emptyList() }
