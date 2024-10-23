package io.kresult.core

import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.Success

/**
 * Transforms a Kotlin native [kotlin.Result] to a [KResult]
 *
 * The resulting type will always have a [Throwable] on the [Failure] side, as Kotlin Results are more restrictive. Keep
 * in mind, that if the failure side is transformed to a type not implementing [Throwable], it can not be transformed
 * back to a Kotlin [Result] using [KResult.asResult].
 *
 * @see KResult.asResult to transform a [KResult] to a Kotlin [Result]
 */
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
 * @see Result.asKResult to transform a Kotlin [Result] to a [KResult]
 */
fun <E : Throwable, T> KResult<E, T>.asResult(): Result<T> =
  fold(
    { t -> Result.failure(t) },
    { e -> Result.success(e) }
  )

/**
 * Transforms any element to a [KResult], carrying the given element on [Failure] side
 */
fun <A> A.asFailure(): Failure<A> =
  Failure(this)

/**
 * Transforms any element to a [MultiErrorKResult], carrying a list of elements on [Failure] side
 *
 * @since 0.2.0
 */
fun <A> A.asFailureList(): Failure<List<A>> =
  Failure(listOf(this))

/**
 * Transforms any element to a [KResult], carrying the given element on [Success] side
 */
fun <A> A.asSuccess(): Success<A> =
  Success(this)

/**
 * Transforms any element to a [MultiValueKResult], carrying a list of elements on [Success] side
 *
 * @since 0.2.0
 */
fun <A> A.asSuccessList(): Success<List<A>> =
  Success(listOf(this))