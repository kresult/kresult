package io.kresult.core

import io.kresult.core.KResult.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

/**
 * Generic representation of a result. Entrypoint for most functionality.
 *
 * <!--- TEST_NAME KResultKnitTest -->
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
   * <!--- KNIT example-result-01.kt -->
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
   * <!--- KNIT example-result-02.kt -->
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
   * <!--- KNIT example-result-03.kt -->
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
   * <!--- KNIT example-result-04.kt -->
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
   * <!--- KNIT example-result-05.kt -->
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
   * <!--- KNIT example-result-06.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param action to run on successful results.
   */
  inline fun onSuccess(action: (success: T) -> Unit): KResult<E, T> {
    contract {
      callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return also {
      when (it) {
        is Failure -> Unit
        is FailureWithValue -> Unit
        is Success -> action(it.value)
      }
    }
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
   * <!--- KNIT example-result-07.kt -->
   * <!--- TEST lines.isEmpty() -->
   *
   * @param action to run on failure results.
   */
  inline fun onFailure(action: (failure: E) -> Unit): KResult<E, T> {
    contract {
      callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return also {
      when (it) {
        is Failure -> action(it.error)
        is FailureWithValue -> action(it.error)
        is Success -> Unit
      }
    }
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
   * <!--- KNIT example-result-08.kt -->
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
   * <!--- KNIT example-result-09.kt -->
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
   * <!--- KNIT example-result-10.kt -->
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
     * <!--- KNIT example-result-11.kt -->
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
   *
   * @see Failed for replacement
   */
  @Deprecated(message = "Deprecated since 0.5.0", replaceWith = ReplaceWith(expression = "Failed"))
  sealed interface HasError<out E> {
    val error: E
  }

  /**
   * Indicates that the [Result] is failed
   *
   * Used for generic matching of [Failure] and [FailureWithValue]. Replaces [HasError].
   *
   * @since 0.5.0
   */
  sealed interface Failed<out E> {
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
   * <!--- KNIT example-result-12.kt -->
   * <!--- TEST lines.isEmpty() -->
   */
  class Success<out T>(
    val value: T,
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
    override val error: E,
  ) : KResult<E, Nothing>(), HasError<E>, Failed<E> {

    override fun toString(): String = "KResult.${this::class.simpleName}($error)"

    override fun equals(other: Any?): Boolean =
      other is Failure<*> && error == other.error

    override fun hashCode(): Int {
      return error?.hashCode() ?: 0
    }

    /**
     * Extends an existing [Failure] with a [value] to produce a [FailureWithValue]
     */
    fun <T> withValue(value: T): FailureWithValue<E, T> =
      FailureWithValue(error, value)
  }

  /**
   * Represents a failed [KResult] that (still) carries its original [Success] value.
   *
   * Instances of this can be interpreted as an extension of [Failure], but with a [value]. To match both, [Failure]
   * and [FailureWithValue], either call [isFailure] (which will be true for both), or match with
   */
  class FailureWithValue<out E, out T>(
    override val error: E,
    val value: T,
  ) : KResult<E, T>(), HasError<E>, Failed<E> {

    override fun toString(): String = "KResult.${this::class.simpleName}($error, $value)"

    override fun equals(other: Any?): Boolean =
      other is FailureWithValue<*, *> && error == other.error

    override fun hashCode(): Int {
      return error?.hashCode() ?: 0
    }
  }
}
