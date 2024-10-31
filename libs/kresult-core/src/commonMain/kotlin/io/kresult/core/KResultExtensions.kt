package io.kresult.core

import io.kresult.core.KResult.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Transforms a successful result into another result through the provided transformation function.
 * If the original result is a failure, it is returned as-is. For [FailureWithValue], the value
 * is transformed but maintains the error state.
 *
 * <!--- TEST_NAME KResultExtensionsKnitTest -->
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.flatMap
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success(5)
 *     .flatMap { value ->
 *       if (value > 0) KResult.Success(value * 2)
 *       else KResult.Failure("Value must be positive")
 *     } shouldBe KResult.Success(10)
 * }
 * ```
 * <!--- KNIT example-flatmap-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param f The transformation function to apply to successful values
 * @return A new [KResult] instance after applying the transformation
 */
@OptIn(ExperimentalContracts::class)
inline fun <E, T, T1> KResult<E, T>.flatMap(
  f: (success: T) -> KResult<E, T1>
): KResult<E, T1> {
  contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
  return when (this) {
    is Success ->
      f(value)

    is Failure ->
      Failure(error)

    is FailureWithValue ->
      f(value).fold(
        { _ -> Failure(error) },
        { innerSuccess -> FailureWithValue(error, innerSuccess) },
      )
  }
}

/**
 * Transforms a failure result into another result through the provided transformation function.
 * If the original result is a success, it is returned as-is. For [FailureWithValue], the error
 * is transformed but maintains the original value.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.flatMapFailure
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Failure("error")
 *     .flatMapFailure { err ->
 *       if (err.length > 3) KResult.Success("recovered")
 *       else KResult.Failure("unrecoverable")
 *     } shouldBe KResult.Success("recovered")
 * }
 * ```
 * <!--- KNIT example-flatmapfailure-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param f The transformation function to apply to failure values
 * @return A new [KResult] instance after applying the transformation
 */
@OptIn(ExperimentalContracts::class)
fun <E, T, E1> KResult<E, T>.flatMapFailure(
  f: (failure: E) -> KResult<E1, T>
): KResult<E1, T> {
  contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
  return when (this) {
    is Failure ->
      f(this.error)

    is Success ->
      this

    is FailureWithValue ->
      f(error).fold(
        { innerFail -> FailureWithValue(innerFail, value) },
        { Success(value) },
      )
  }
}

/**
 * Flattens a nested result where the success value is itself a result.
 * This operation is equivalent to applying [flatMap] with the identity function.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.flatten
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   val nested = KResult.Success(KResult.Success(42))
 *   nested.flatten() shouldBe KResult.Success(42)
 * }
 * ```
 * <!--- KNIT example-flatten-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @return A flattened [KResult] instance
 */
fun <E, T> KResult<E, KResult<E, T>>.flatten(): KResult<E, T> =
  flatMap { it }

/**
 * Flattens a nested result where the failure value is itself a result. This operation is equivalent to applying [flatMapFailure] with
 * the identity function.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.flattenFailure
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   val error = KResult.Failure("inner error")
 *   val nested = KResult.Failure(error)
 *   nested.flattenFailure() shouldBe KResult.Failure("inner error")
 *
 *   val success: KResult<KResult<Nothing, Int>, Int> = KResult.Success(42)
 *   success.flattenFailure() shouldBe KResult.Success(42)
 * }
 * ```
 * <!--- KNIT example-flattenfailure-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @return A flattened [KResult] instance
 */
fun <E, T> KResult<KResult<E, T>, T>.flattenFailure(): KResult<E, T> =
  flatMapFailure { it }

/**
 * Filters a successful result based on a predicate function, transforming it to a failure
 * if the predicate returns false. The failure value is generated using the provided function.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.filter
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success(42)
 *     .filter(
 *       failureFn = { value -> "Value $value is not positive" },
 *       f = { it > 0 }
 *     ) shouldBe KResult.Success(42)
 *
 *   KResult.Success(-1)
 *     .filter(
 *       failureFn = { value -> "Value $value is not positive" },
 *       f = { it > 0 }
 *     ) shouldBe KResult.Failure("Value -1 is not positive")
 * }
 * ```
 * <!--- KNIT example-filter-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param failureFn Function to generate the failure value if the predicate returns false
 * @param f The predicate function to test the success value
 * @return Original result if predicate returns true, otherwise a failure with the generated error
 */
@OptIn(ExperimentalContracts::class)
inline fun <E, T> KResult<E, T>.filter(
  failureFn: (success: T) -> E,
  f: (success: T) -> Boolean
): KResult<E, T> {
  contract {
    callsInPlace(f, InvocationKind.UNKNOWN)
    callsInPlace(failureFn, InvocationKind.UNKNOWN)
  }
  return fold(
    { Failure(it) },
    {
      if (f(it)) {
        val success: Success<T> = Success(it)
        success
      } else {
        Failure(failureFn(it))
      }
    }
  )
}

/**
 * Filters a successful result based on a predicate function, transforming it to a failure
 * with a constant error value if the predicate returns false.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.filter
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success(42)
 *     .filter(
 *       failureValue = "Invalid value",
 *       f = { it > 0 }
 *     ) shouldBe KResult.Success(42)
 * }
 * ```
 * <!--- KNIT example-filter-02.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param failureValue The error value to use if the predicate returns false
 * @param f The predicate function to test the success value
 * @return Original result if predicate returns true, otherwise a failure with the provided error
 */
@OptIn(ExperimentalContracts::class)
inline fun <E, T> KResult<E, T>.filter(
  failureValue: E,
  f: (success: T) -> Boolean
): KResult<E, T> {
  contract {
    callsInPlace(f, InvocationKind.UNKNOWN)
  }
  return fold(
    { Failure(it) },
    {
      if (f(it)) {
        val success: Success<T> = Success(it)
        success
      } else {
        Failure(failureValue)
      }
    }
  )
}

/**
 * Filters [KResult] of a nullable value and transforms (filters) it to [Failure] of the [failureFn], if the value is
 * `null`. The resulting entity will be non-nullable, no matter if it's a [Failure] or [Success]
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.filterNull
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success("test")
 *     .filterNull { "Value was null" } shouldBe KResult.Success("test")
 *
 *   KResult.Success(null)
 *     .filterNull { "Value was null" } shouldBe KResult.Failure("Value was null")
 * }
 * ```
 * <!--- KNIT example-filternull-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param failureFn Function that provides the failure value when the success value is null
 * @return A [KResult] with non-nullable success type
 * @since 0.5.0
 */
fun <E, T> KResult<E, T?>.filterNull(failureFn: () -> E): KResult<E, T> =
  flatMap { value ->
    if (value == null) {
      Failure(failureFn())
    } else {
      Success(value)
    }
  }

// getters for success & failure

@Deprecated("Deprecated since 0.2.0", replaceWith = ReplaceWith("getOrDefault(default)"))
inline infix fun <E, T> KResult<E, T>.getOrElse(default: (E) -> T): T =
  getOrDefault(default)

/**
 * Returns the value of the [Success] side, or the result of the [default] function otherwise. Note: For
 * [FailureWithValue], this still returns the default value as it is semantically a failure.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.getOrDefault
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success(42)
 *     .getOrDefault { -1 } shouldBe 42
 *
 *   KResult.Failure("error")
 *     .getOrDefault { -1 } shouldBe -1
 *
 *   KResult.FailureWithValue("error", 42)
 *     .getOrDefault { -1 } shouldBe -1
 * }
 * ```
 * <!--- KNIT example-getor-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param default Function to provide a default value in case of failure
 * @return The success value or the computed default value
 */
@OptIn(ExperimentalContracts::class)
inline infix fun <E, T> KResult<E, T>.getOrDefault(default: (E) -> T): T {
  contract { callsInPlace(default, InvocationKind.AT_MOST_ONCE) }
  return when (this) {
    is Failure -> default(this.error)
    is Success -> this.value
    is FailureWithValue -> default(this.error)
  }
}

/**
 * Returns the failure value if present, otherwise returns the result of the default function. Similar to
 * [getOrDefault] but for the failure side.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.failureOrDefault
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Failure("error")
 *     .failureOrDefault { "no error" } shouldBe "error"
 *
 *   KResult.Success(42)
 *     .failureOrDefault { "no error" } shouldBe "no error"
 *
 *   KResult.FailureWithValue("error", 42)
 *     .failureOrDefault { "no error" } shouldBe "error"
 * }
 * ```
 * <!--- KNIT example-failureor-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param default Function to provide a default error value in case of success
 * @return The failure value or the computed default value
 */
@OptIn(ExperimentalContracts::class)
inline infix fun <E, T> KResult<E, T>.failureOrDefault(default: (T) -> E): E {
  contract { callsInPlace(default, InvocationKind.AT_MOST_ONCE) }
  return when (this) {
    is Failure -> this.error
    is Success -> default(this.value)
    is FailureWithValue -> this.error
  }
}

/**
 * If a [KResult] has a [Throwable] on failure side, this either returns the [Success.value] or throws the
 * [Failure.error].
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.getOrThrow
 * import io.kotest.assertions.throwables.shouldThrow
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success(42)
 *     .getOrThrow() shouldBe 42
 *
 *   val exception = RuntimeException("test error")
 *   shouldThrow<RuntimeException> {
 *     KResult.Failure(exception).getOrThrow()
 *   }.message shouldBe "test error"
 *
 *   shouldThrow<RuntimeException> {
 *     KResult.FailureWithValue(exception, 42).getOrThrow()
 *   }.message shouldBe "test error"
 * }
 * ```
 * <!--- KNIT example-getorthrow-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @return The success value
 * @throws E if the result is a failure
 */
fun <E : Throwable, T> KResult<E, T>.getOrThrow(): T {
  return when (this) {
    is Failure -> throw this.error
    is Success -> this.value
    is FailureWithValue -> throw this.error
  }
}

/**
 * Compares two results based on their values. Failures are considered less than successes. For same result types, the
 * underlying values are compared using their natural ordering.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.compareTo
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   val success1 = KResult.Success(1)
 *   val success2 = KResult.Success(2)
 *   (success1 < success2) shouldBe true
 *
 *   val failure1 = KResult.Failure("a")
 *   val failure2 = KResult.Failure("b")
 *   (failure1 < failure2) shouldBe true
 *
 *   (failure1 < success1) shouldBe true  // Failures always less than successes
 * }
 * ```
 * <!--- KNIT example-compare-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param other The result to compare with
 * @return A negative number if this is less than other, zero if equal, positive if greater
 */
operator fun <E : Comparable<E>, T : Comparable<T>> KResult<E, T>.compareTo(other: KResult<E, T>): Int =
  fold(
    { a1 -> other.fold({ a2 -> a1.compareTo(a2) }, { -1 }) },
    { b1 -> other.fold({ 1 }, { b2 -> b1.compareTo(b2) }) }
  )

/**
 * Merges a result where both failure and success types are the same. This allows treating success and failure cases
 * uniformly when they contain the same type.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.merge
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success("success").merge() shouldBe "success"
 *   KResult.Failure("failure").merge() shouldBe "failure"
 *   KResult.FailureWithValue("error", "value").merge() shouldBe "error"
 * }
 * ```
 * <!--- KNIT example-merge-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @return The value from either the success or failure side
 */
fun <T> KResult<T, T>.merge(): T =
  fold(
    { it },
    { it }
  )

/**
 * Combines two results using the provided combining functions for both success and failure cases. If either result is
 * a failure, the combined result will be a failure using the provided failure combining function.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.core.combine
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   // Combining successes
 *   val s1: KResult<String, Int> = KResult.Success(1)
 *   val s2: KResult<String, Int> = KResult.Success(2)
 *   s1.combine(
 *     s2,
 *     combineFailure = { e1, e2 -> "$e1 and $e2" },
 *     combineSuccess = { v1, v2 -> v1 + v2 }
 *   ) shouldBe KResult.Success(3)
 *
 *   // Combining failures
 *   val f1: KResult<String, Int> = KResult.Failure("error1")
 *   val f2: KResult<String, Int> = KResult.Failure("error2")
 *   f1.combine(
 *     f2,
 *     combineFailure = { e1, e2 -> "$e1 and $e2" },
 *     combineSuccess = { v1, v2 -> v1 + v2 }
 *   ) shouldBe KResult.Failure("error1 and error2")
 *
 *   // Mixing success and failure
 *   val mixed = s1.combine(
 *     f1,
 *     combineFailure = { e1, e2 -> "$e1 and $e2" },
 *     combineSuccess = { v1, v2 -> v1 + v2 }
 *   ) shouldBe KResult.Failure("error1")
 * }
 * ```
 * <!--- KNIT example-combine-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 *
 * @param other The result to combine with
 * @param combineFailure Function to combine two failure values
 * @param combineSuccess Function to combine two success values
 * @return A combined result
 */
fun <E, T> KResult<E, T>.combine(
  other: KResult<E, T>,
  combineFailure: (E, E) -> E,
  combineSuccess: (T, T) -> T,
): KResult<E, T> =
  when (val one = this) {
    is Failure -> when (other) {
      is Failure -> Failure(combineFailure(one.error, other.error))
      is FailureWithValue -> Failure(combineFailure(one.error, other.error))
      is Success -> one
    }

    is FailureWithValue -> when (other) {
      is Failure -> Failure(combineFailure(one.error, other.error))
      is FailureWithValue -> Failure(combineFailure(one.error, other.error))
      is Success -> one
    }

    is Success -> when (other) {
      is Failure -> other
      is FailureWithValue -> other
      is Success -> Success(combineSuccess(one.value, other.value))
    }
  }