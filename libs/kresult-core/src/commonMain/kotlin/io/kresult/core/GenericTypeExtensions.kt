package io.kresult.core

import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.Success

// create from generic types outside the library

fun <T> Result<T>.asKResult(): KResult<Throwable, T> =
  fold(
    { t -> Success(t) },
    { e -> Failure(e) },
  )

/**
 * Transforms a [KResult] to a Kotlin native [kotlin.Result]
 *
 * As Kotlin's [Result] strinctly expects a [Throwable] as error / failure type, this method is only available on
 * [KResult] instances having a [Throwable] subtype on their [Failure] side.
 *
 * @since 0.2.0
 * @see KResult.asKResult to transform a Kotlin [Result] to a [KResult]
 */
fun <E : Throwable, T> KResult<E, T>.asResult(): Result<T> =
  fold(
    { t -> Result.failure(t) },
    { e -> Result.success(e) }
  )
fun <A> A.asFailure(): KResult<A, Nothing> =
  Failure(this)

fun <A> A.asSuccess(): KResult<Nothing, A> =
  Success(this)