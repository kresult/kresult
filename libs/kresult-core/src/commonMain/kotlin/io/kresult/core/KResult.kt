package io.kresult.core

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

/**
 * <!--- TEST_NAME KResultKnitTest -->
 *
 * [KResult] and its inheritors [Success] and [Failure] provide an opinionated, functional result type. While Kotlin has
 * its own [kotlin.Result] already, the intent of [KResult] is to be more functional, usable and better integrated.
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
 *
 * fun test() {
 *   // by instance
 *   KResult.Success("test")
 *
 *   // by extension function
 *   "test".asSuccess()
 *
 *   // by catching exceptions
 *   KResult.catch {
 *     throw RuntimeException("throws")
 *   }
 *
 *   // from nullable
 *   KResult.fromNullable(null) {
 *     RuntimeException("Value can not be null")
 *   }
 *
 *   // from Kotlin Result
 *   Result.success("test")
 *     .asKResult()
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
 * import arrow.core.Either
 * import io.kresult.arrow.toKResult
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
 *
 * fun test() {
 *   // map
 *   KResult.Success(2)
 *     .map { it - 1 }
 *
 *   // flatMap
 *   KResult.Success("some-p4ss!").flatMap {
 *     if (it.length > 8) {
 *       KResult.Success(it)
 *     } else {
 *       KResult.Failure(RuntimeException("Password is too short"))
 *     }
 *   }
 *
 *   // filter
 *   KResult.Success("some-p4ss!").filter(
 *     { it.isNotBlank() },
 *     { RuntimeException("String is empty") }
 *   )
 *
 *   // flatten
 *   val nested: KResult<Throwable, KResult<Throwable, Int>> =
 *     KResult.Success(KResult.Success(2))
 *
 *   val flattened: KResult<Throwable, Int> =
 *     nested.flatten()
 * }
 * ```
 * <!--- KNIT example-result-03.kt -->
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
  }

  /**
   * Success component for destructuring declaration
   *
   * @return the [Success.value] if [KResult] is a success, null otherwise
   */
  open operator fun component2(): T? = when (this) {
    is Success -> value
    is Failure -> null
  }

  /**
   * Indicates if a [KResult] is a [Failure]
   *
   * ```kotlin
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   KResult.Failure("test").isFailure() shouldBe true
   *   KResult.Success("test").isFailure() shouldBe false
   * }
   * ```
   * <!--- KNIT example-result-04.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun isFailure(): Boolean {
    contract {
      returns(true) implies (this@KResult is Failure<E>)
      returns(false) implies (this@KResult is Success<T>)
    }
    return this@KResult is Failure<E>
  }

  /**
   * Indicates if a [KResult] is a [Success]
   *
   * ```kotlin
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   KResult.Success("test").isSuccess() shouldBe true
   *   KResult.Failure("test").isSuccess() shouldBe false
   * }
   * ```
   * example-result-01.kt
   *
   * <!--- KNIT example-result-05.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun isSuccess(): Boolean {
    contract {
      returns(true) implies (this@KResult is Success<T>)
      returns(false) implies (this@KResult is Failure<E>)
    }
    return this@KResult is Success<T>
  }

  /**
   * Transforms a [KResult] into a value of [R].
   *
   * ```kotlin
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
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
   * <!--- KNIT example-result-06.kt -->
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
    }
  }

  /**
   * Maps (transforms) the success value [T] to a new value [R]
   *
   * ```kotlin
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   KResult.Success(2)
   *     .map {
   *       it * it
   *     }
   *     .getOrNull() shouldBe 4
   * }
   * ```
   * <!--- KNIT example-result-07.kt -->
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
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   KResult.Failure(2)
   *     .mapFailure {
   *       it * it
   *     }
   *     .failureOrNull() shouldBe 4
   * }
   * ```
   * <!--- KNIT example-result-08.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param f transform the [KResult.Success] type [T] to [R].
   */
  inline fun <R> mapFailure(f: (E) -> R): KResult<R, T> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
      is Failure -> Failure(f(error))
      is Success -> Success(value)
    }
  }

  /**
   * Runs an action (side-effect) when the [KResult] is a [Success]
   *
   * ```kotlin
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
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
   * <!--- KNIT example-result-09.kt -->
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
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
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
   * <!--- KNIT example-result-10.kt -->
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
   * Runs the [Success.value], if the [KResult] is a success or [null] otherwise
   *
   * ```kotlin
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   KResult.Success("test-success")
   *     .getOrNull() shouldBe "test-success"
   *
   *   KResult.Failure("test-failure")
   *     .getOrNull() shouldBe null
   * }
   * ```
   * <!--- KNIT example-result-11.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun getOrNull(): T? {
    contract {
      returns(null) implies (this@KResult is Failure<E>)
      returnsNotNull() implies (this@KResult is Success<T>)
    }
    return getOrElse { null }
  }

  /**
   * Runs the [Failure.error], if the [KResult] is a failure or [null] otherwise
   *
   * ```kotlin
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   KResult.Failure("test-failure")
   *     .failureOrNull() shouldBe "test-failure"
   *
   *   KResult.Success("test-success")
   *     .failureOrNull() shouldBe null
   * }
   * ```
   * <!--- KNIT example-result-12.kt -->
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
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   KResult.Failure("test").swap() shouldBe KResult.Success("test")
   *   KResult.Success("test").swap() shouldBe KResult.Failure("test")
   * }
   * ```
   * <!--- KNIT example-result-13.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  fun swap(): KResult<T, E> =
    fold({ Success(it) }, { Failure(it) })

  companion object {

    @JvmStatic
    inline fun <T> catch(f: () -> T): KResult<Throwable, T> =
      kotlin.runCatching { f() }
        .asKResult()

    fun <T, E> fromNullable(value: T?, errFn: () -> E): KResult<E, T> =
      value
        ?.asSuccess()
        ?: errFn().asFailure()

    fun <T> fromNullable(value: T?): KResult<Throwable, T> =
      fromNullable(value) {
        NullPointerException("Value null!")
      }

  }

  /**
   * Represents a failed [KResult]
   */
  class Failure<out E>(val error: E) : KResult<E, Nothing>() {

    override fun toString(): String = "${this::class.simpleName}($error)"

    override fun equals(other: Any?): Boolean =
      if (other is Failure<*>)
        error == other.error
      else
        false

    override fun hashCode(): Int {
      return error?.hashCode() ?: 0
    }
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
   * import io.kotest.matchers.shouldBe
   * import io.kresult.core.KResult
   *
   * fun test() {
   *   val res: KResult<Nothing, Unit> =
   *     KResult.Success.unit
   *
   *   res.isSuccess() shouldBe true
   * }
   * ```
   * <!--- KNIT example-result-14.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  class Success<out T>(val value: T) : KResult<Nothing, T>() {

    override fun toString(): String = "${this::class.simpleName}($value)"

    override fun equals(other: Any?): Boolean =
      if (other is Success<*>)
        value == other.value
      else
        false

    override fun hashCode(): Int {
      return value?.hashCode() ?: 0
    }

    companion object {
      val unit: KResult<Nothing, Unit> =
        Success(Unit)
    }
  }
}


