package io.kresult.core

import io.kresult.core.KResult.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

/**
 * [KResult] and its inheritors [Success] and [Failure] provide an opinionated, functional result type. While Kotlin has
 * its own [kotlin.Result] already, the intent of [KResult] is to be more functional, usable and better integrated.
 *
 * <!--- TEST_NAME KResultKnitTest -->
 *
 * ## Creating Results
 *
 * Results can be created by constructing [Success] or [Failure] directly, but there is a variety of helpers and third
 * party types to create results from:
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.asKResult
 * import io.kresult.core.asSuccess
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   // by instance
 *   KResult.Success("test")
 *     .isSuccess() shouldBe true
 *
 *   // by extension function
 *   "test".asSuccess()
 *     .isSuccess() shouldBe true
 *
 *   // by catching exceptions
 *   KResult
 *     .catch {
 *       throw RuntimeException("throws")
 *     }
 *     .isSuccess() shouldBe false
 *
 *   // from nullable
 *   KResult
 *     .fromNullable(null) {
 *       RuntimeException("Value can not be null")
 *     }
 *     .isSuccess() shouldBe false
 *
 *   // from Kotlin Result
 *   Result.success("test")
 *     .asKResult()
 *     .isSuccess() shouldBe true
 * }
 * ```
 * <!--- KNIT example-result-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * ### From Extensions
 *
 * There are more result builders with extensions, e.g. from `kresult-arrow`:
 *
 * ```kotlin
 * import io.kresult.integration.arrow.toKResult
 * import arrow.core.Either
 *
 * fun test() {
 *   Either.Right("test")
 *     .toKResult()
 * }
 * ```
 * <!--- KNIT example-result-02.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * ## Mapping & Transformation
 *
 * Results can be transformed in several ways. At the core, [KResult] implements functional mapping with [flatMap],
 * [map], [flatten], [filter], etc. Some of these are extension functions that need to be imported.:
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.filter
 * import io.kresult.core.flatMap
 * import io.kresult.core.flatten
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   // map
 *   KResult.Success(3)
 *     .map { it - 1 }
 *     .getOrNull() shouldBe 2
 *
 *   // flatMap
 *   KResult.Success("some-p4ss!")
 *     .flatMap {
 *       if (it.length > 8) {
 *         KResult.Success(it)
 *       } else {
 *         KResult.Failure(RuntimeException("Password is too short"))
 *       }
 *     } shouldBe KResult.Success("some-p4ss!")
 *
 *   // filter
 *   KResult.Success("some-p4ss!")
 *     .filter(RuntimeException("String is empty")) {
 *       it.isNotBlank()
 *     } shouldBe KResult.Success("some-p4ss!")
 *
 *   // flatten
 *   KResult.Success(KResult.Success(2))
 *     .flatten() shouldBe KResult.Success(2)
 * }
 * ```
 * <!--- KNIT example-result-03.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * ## Getting values or errors
 *
 * As a [KResult] is always either a [Success] or a [Failure] / [FailureWithValue], a simple, kotlin-native `when`
 * expression or a destructuring declaration can be used to get the [Success.value] or [Failure.error]:
 *
 * **Heads up:** If you destructure a [FailureWithValue], you'll get an `error` **AND** a `value` back!
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.KResult.Failure
 * import io.kresult.core.KResult.FailureWithValue
 * import io.kresult.core.KResult.Success
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   val res: KResult<String, Int> = Success(2)
 *
 *   // when expression
 *   when (res) {
 *     is Success -> "success: ${res.value}"
 *     is Failure -> "failure: ${res.error}"
 *     is FailureWithValue -> "failure: ${res.error} (original value was ${res.value})"
 *   } shouldBe "success: 2"
 *
 *   // destructuring call
 *   val (error, value) = res
 *
 *   error shouldBe null
 *   value shouldBe 2
 * }
 * ```
 * <!--- KNIT example-result-04.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * ### Convenience Methods
 *
 * Additionally, convenience methods like [fold], [getOrNull], [getOrDefault], or [getOrThrow] for results that have a
 * [Throwable] on failure side.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.getOrDefault
 * import io.kresult.core.getOrThrow
 * import io.kotest.assertions.throwables.shouldThrow
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   // fold
 *   KResult.Success(2).fold(
 *     { "failure: $it" },
 *     { "success: $it" }
 *   ) shouldBe "success: 2"
 *
 *   // getOrNull
 *   KResult.Success(2).getOrNull() shouldBe 2
 *   KResult.Failure("error").getOrNull() shouldBe null
 *
 *   // getOrElse
 *   KResult.Success(2).getOrDefault { -1 } shouldBe 2
 *   KResult.Failure("error").getOrDefault { -1 } shouldBe -1
 *
 *   // getOrThrow
 *   KResult.Success(2).getOrThrow() shouldBe 2
 *   shouldThrow<RuntimeException> {
 *     KResult.Failure(RuntimeException("test")).getOrThrow()
 *   }
 * }
 * ```
 * <!--- KNIT example-result-05.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * ## Multiple Failures or Success values
 *
 * Returning a **list of successful values** with a result, as well as indicating **multiple failure reasons** are
 * common cases. If we are, for example **validating user input**, we want to indicate all validations that failed,
 * not just the first.
 *
 * Because of the flexibility of [KResult], this can easily be achieved by just representing the [Success] or [Failure]
 * side with a [List] respectively. Examples could be `KResult<List<Error>, String>` or
 * `KResult<List<Error>, List<String>>` if we have multiple success results as well. As these type definition can become
 * rather complex, predefined typealiases can be used:
 *
 * - [MultiErrorKResult] if failure side carries a list
 * - [MultiValueKResult] if success side carries a list
 *
 * To make working with success or failure lists easier, there are a few conveninet helpers. Keep in mind that the
 * [KResult.validate] extension can only be used with [MultiErrorKResult] or more generally, if you have a [List] on
 * the [Failure] side:
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
 * <!--- KNIT example-result-06.kt -->
 * <!--- TEST lines.isEmpty() -->
 */
@OptIn(ExperimentalContracts::class)
sealed class KResult<out E, out T> {

  /**
   * Failure component for destructuring declaration
   *
   * @return the [Failure.error] if [KResult] is a failure, null otherwise
   */
  open operator fun component1(): E? = when (this) {
    is Success -> null
    is Failure -> error
    is FailureWithValue -> error
  }

  /**
   * Success component for destructuring declaration
   *
   * @return the [Success.value] if [KResult] is a success, null otherwise
   */
  open operator fun component2(): T? = when (this) {
    is Success -> value
    is Failure -> null
    is FailureWithValue -> value
  }

  /**
   * Indicates if a [KResult] is a [Failure]
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   KResult.Failure("test").isFailure() shouldBe true
   *   KResult.Success("test").isFailure() shouldBe false
   * }
   * ```
   * <!--- KNIT example-result-07.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun isFailure(): Boolean {
    contract {
      returns(true) implies (this@KResult is Failure<E>)
      returns(true) implies (this@KResult is FailureWithValue<E, T>)
      returns(false) implies (this@KResult is Success<T>)
    }
    return this@KResult is Failure<E> || this@KResult is FailureWithValue<E, T>
  }

  /**
   * Indicates if a [KResult] is a [Success]
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   KResult.Success("test").isSuccess() shouldBe true
   *   KResult.Failure("test").isSuccess() shouldBe false
   * }
   * ```
   * <!--- KNIT example-result-08.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun isSuccess(): Boolean {
    contract {
      returns(false) implies (this@KResult is Failure<E>)
      returns(false) implies (this@KResult is FailureWithValue<E, T>)
      returns(true) implies (this@KResult is Success<T>)
    }
    return this@KResult is Success<T>
  }

  /**
   * Transforms a [KResult] into a value of [R].
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   * import kotlin.test.fail
   *
   * fun test() {
   *   KResult.Success(1)
   *     .fold(
   *       { fail("Cannot be left") },
   *       { it + 1 }
   *     ) shouldBe 2
   * }
   * ```
   * <!--- KNIT example-result-09.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param ifFailure transform the [KResult.Failure] type [E] to [R].
   * @param ifSuccess transform the [KResult.Success] type [T] to [R].
   * @return the transformed value [R] by applying [ifFailure] or [ifSuccess].
   */
  inline fun <R> fold(ifFailure: (failure: E) -> R, ifSuccess: (success: T) -> R): R {
    contract {
      callsInPlace(ifFailure, InvocationKind.AT_MOST_ONCE)
      callsInPlace(ifSuccess, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
      is Success -> ifSuccess(value)
      is Failure -> ifFailure(error)
      is FailureWithValue -> ifFailure(error)
    }
  }

  /**
   * Maps (transforms) the success value [T] to a new value [R]
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   KResult.Success(2)
   *     .map {
   *       it * it
   *     }
   *     .getOrNull() shouldBe 4
   * }
   * ```
   * <!--- KNIT example-result-10.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param f transform the [KResult.Success] type [T] to [R].
   */
  inline fun <R> map(f: (success: T) -> R): KResult<E, R> {
    contract {
      callsInPlace(f, InvocationKind.AT_MOST_ONCE)
    }
    return flatMap { Success(f(it)) }
  }

  /**
   * Maps (transforms) the failure value [E] to a new value [R]
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   KResult.Failure(2)
   *     .mapFailure {
   *       it * it
   *     }
   *     .failureOrNull() shouldBe 4
   * }
   * ```
   * <!--- KNIT example-result-11.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param f transform the [KResult.Success] type [T] to [R].
   */
  inline fun <R> mapFailure(f: (E) -> R): KResult<R, T> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
      is Failure -> Failure(f(error))
      is Success -> Success(value)
      is FailureWithValue -> FailureWithValue(f(error), value)
    }
  }

  /**
   * Runs an action (side-effect) when the [KResult] is a [Success]
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   var result = ""
   *
   *   KResult.Success("test-success")
   *     .onSuccess {
   *       result += it
   *     }
   *
   *   result shouldBe "test-success"
   * }
   * ```
   * <!--- KNIT example-result-12.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param action to run on successful results.
   */
  inline fun onSuccess(action: (success: T) -> Unit): KResult<E, T> {
    contract {
      callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return also { if (it.isSuccess()) action(it.value) }
  }

  /**
   * Runs an action (side-effect) when the [KResult] is a [Failure]
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   var result = ""
   *
   *   KResult.Failure("test-failure")
   *     .onFailure {
   *       result += it
   *     }
   *
   *   result shouldBe "test-failure"
   * }
   * ```
   * <!--- KNIT example-result-13.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param action to run on failure results.
   */
  inline fun onFailure(action: (failure: E) -> Unit): KResult<E, T> {
    contract {
      callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return also { if (it.isFailure()) action(it.error) }
  }

  /**
   * Runs the [Success.value], if the [KResult] is a success or `null` otherwise
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   KResult.Success("test-success")
   *     .getOrNull() shouldBe "test-success"
   *
   *   KResult.Failure("test-failure")
   *     .getOrNull() shouldBe null
   * }
   * ```
   * <!--- KNIT example-result-14.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun getOrNull(): T? {
    contract {
      returns(null) implies (this@KResult is Failure<E>)
      returnsNotNull() implies (this@KResult is Success<T>)
    }
    return getOrDefault { null }
  }

  /**
   * Runs the [Failure.error], if the [KResult] is a failure or `null` otherwise
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   KResult.Failure("test-failure")
   *     .failureOrNull() shouldBe "test-failure"
   *
   *   KResult.Success("test-success")
   *     .failureOrNull() shouldBe null
   * }
   * ```
   * <!--- KNIT example-result-15.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun failureOrNull(): E? {
    contract {
      returnsNotNull() implies (this@KResult is Failure<E>)
      returns(null) implies (this@KResult is Success<T>)
    }
    return fold(
      { it },
      { null }
    )
  }

  /**
   * Swap the parameters ([T] and [E]) of this [Result].
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   KResult.Failure("test").swap() shouldBe KResult.Success("test")
   *   KResult.Success("test").swap() shouldBe KResult.Failure("test")
   * }
   * ```
   * <!--- KNIT example-result-16.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun swap(): KResult<T, E> =
    fold({ Success(it) }, { Failure(it) })

  companion object {

    @JvmStatic
    inline fun <T> catch(f: () -> T): KResult<Throwable, T> =
      kotlin.runCatching { f() }
        .asKResult()

    fun <E, T> fromNullable(value: T?, errFn: () -> E): KResult<E, T> =
      value
        ?.asSuccess()
        ?: errFn().asFailure()

    fun <T> fromNullable(value: T?): KResult<Throwable, T> =
      fromNullable(value) {
        NullPointerException("Value null!")
      }

    /**
     * Combine two [KResult] instances of same type
     *
     * This is an alternative syntax for calling [KResult.combine] on the first result and applying second as a
     * parameter.
     *
     * ```kotlin
     * import io.kresult.core.KResult
     *
     * fun test() {
     *   val first: KResult<String, String> = KResult.Success("test 1")
     *   val second: KResult<String, String> = KResult.Success("test 2")
     *
     *   KResult.combine(
     *     first,
     *     second,
     *     { f1, f2 -> "$f1, $f2" },
     *     { s1, s2 -> "$s1, $s2" }
     *   )
     * }
     * ```
     * <!--- KNIT example-result-17.kt -->
     * <!--- TEST lines.isEmpty() -->
     */
    fun <E, T> combine(
      first: KResult<E, T>,
      second: KResult<E, T>,
      combineFailure: (E, E) -> E,
      combineSuccess: (T, T) -> T,
    ): KResult<E, T> =
      first.combine(second, combineFailure, combineSuccess)

  }

  /**
   * Indicates that the implementation carries an error
   *
   * Used for unified processing of [Failure] and [FailureWithValue]
   */
  sealed interface HasError<out E> {
    val error: E
  }

  /**
   * Represents a successful [KResult]
   *
   * ## Empty Success
   *
   * An empty success, e.g. an action that can fail or be successful but doesn't return any value,
   * can be represented using:
   *
   * ```kotlin
   * import io.kresult.core.KResult
   * import io.kotest.matchers.shouldBe
   *
   * fun test() {
   *   val res: KResult<Nothing, Unit> =
   *     KResult.Success.unit
   *
   *   res.isSuccess() shouldBe true
   * }
   * ```
   * <!--- KNIT example-result-18.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  class Success<out T>(
    val value: T
  ) : KResult<Nothing, T>() {

    override fun toString(): String = "KResult.${this::class.simpleName}($value)"

    override fun equals(other: Any?): Boolean =
      other is Success<*> && value == other.value

    override fun hashCode(): Int {
      return value?.hashCode() ?: 0
    }

    companion object {
      val unit: KResult<Nothing, Unit> =
        Success(Unit)
    }
  }

  /**
   * Represents a failed [KResult]
   */
  class Failure<out E>(
    override val error: E
  ) : KResult<E, Nothing>(), HasError<E> {

    override fun toString(): String = "KResult.${this::class.simpleName}($error)"

    override fun equals(other: Any?): Boolean =
      other is Failure<*> && error == other.error

    override fun hashCode(): Int {
      return error?.hashCode() ?: 0
    }
  }

  class FailureWithValue<out E, out T>(
    override val error: E,
    val value: T
  ) : KResult<E, T>(), HasError<E> {

    override fun toString(): String = "KResult.${this::class.simpleName}($error, $value)"

    override fun equals(other: Any?): Boolean =
      other is FailureWithValue<*, *> && error == other.error

    override fun hashCode(): Int {
      return error?.hashCode() ?: 0
    }
  }
}
