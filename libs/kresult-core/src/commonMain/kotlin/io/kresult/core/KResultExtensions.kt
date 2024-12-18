package io.kresult.core

import io.kresult.core.KResult.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// flatMap implementations

@OptIn(ExperimentalContracts::class)
inline fun <E, T, T1> KResult<E, T>.flatMap(f: (success: T) -> KResult<E, T1>): KResult<E, T1> {
  contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
  return when (this) {
    is Success ->
      f(value)

    is Failure ->
      Failure(error)

    // If the flatMap(f) results in a failure, we lose type information of the value and need to return a Failure
    // because of that
    is FailureWithValue ->
      f(value).fold(
        { _ -> Failure(error) },
        { innerSuccess -> FailureWithValue(error, innerSuccess) },
      )
  }
}

@OptIn(ExperimentalContracts::class)
fun <E, T, E1> KResult<E, T>.flatMapFailure(f: (failure: E) -> KResult<E1, T>): KResult<E1, T> {
  contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
  return when (this) {
    is Failure ->
      f(this.error)

    is Success ->
      this

    // If the flatMap(f) results in a success, we lose type information of the error and need to return a Success
    // because of that
    is FailureWithValue ->
      f(error).fold(
        { innerFail -> FailureWithValue(innerFail, value) },
        { Success(value) },
      )
  }
}

fun <E, T> KResult<E, KResult<E, T>>.flatten(): KResult<E, T> =
  flatMap { it }

fun <E, T> KResult<KResult<E, T>, T>.flattenFailure(): KResult<E, T> =
  flatMapFailure { it }

@OptIn(ExperimentalContracts::class)
inline fun <E, T> KResult<E, T>.filter(failureFn: (success: T) -> E, f: (success: T) -> Boolean): KResult<E, T> {
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

@OptIn(ExperimentalContracts::class)
inline fun <E, T> KResult<E, T>.filter(failureValue: E, f: (success: T) -> Boolean): KResult<E, T> {
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

// getters for success & failure

@Deprecated("Deprecated since 0.2.0", replaceWith = ReplaceWith("getOrDefault(default)"))
inline infix fun <E, T> KResult<E, T>.getOrElse(default: (E) -> T): T =
  getOrDefault(default)

/**
 * Returns the value of the [Success] side, or the result of the [default] function otherwise
 *
 * @since 0.2.0
 */
@OptIn(ExperimentalContracts::class)
inline infix fun <E, T> KResult<E, T>.getOrDefault(default: (E) -> T): T {
  contract { callsInPlace(default, InvocationKind.AT_MOST_ONCE) }
  return when (this) {
    is Failure -> default(this.error)
    is Success -> this.value
    // we could return a value here, but that would be semantically incorrect, because a Failure with a value is
    // still a failure
    is FailureWithValue -> default(this.error)
  }
}

/**
 * Returns the error of the [Failure] side, or the result of the [default] function otherwise
 *
 * @since 0.2.0
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
 * [Failure.error]
 *
 * @return T if result is a [Success]
 * @throws E if result is a [Failure]
 */
fun <E : Throwable, T> KResult<E, T>.getOrThrow(): T {
  return when (this) {
    is Failure -> throw this.error
    is Success -> this.value
    is FailureWithValue -> throw this.error
  }
}

// comparison

operator fun <E : Comparable<E>, T : Comparable<T>> KResult<E, T>.compareTo(other: KResult<E, T>): Int =
  fold(
    { a1 -> other.fold({ a2 -> a1.compareTo(a2) }, { -1 }) },
    { b1 -> other.fold({ 1 }, { b2 -> b1.compareTo(b2) }) }
  )

// merge / combine

fun <T> KResult<T, T>.merge(): T =
  fold(
    { it },
    { it }
  )

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
