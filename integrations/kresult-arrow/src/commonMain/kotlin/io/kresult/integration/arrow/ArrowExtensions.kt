package io.kresult.integration.arrow

import io.kresult.core.KResult
import arrow.core.Either

/**
 * Transforms an Arrow [Either] to a [KResult]
 *
 * <!--- TEST_NAME ArrowIntegrationsKnitTest -->
 *
 * ```kotlin
 * import arrow.core.Either
 * import io.kotest.matchers.shouldBe
 * import io.kresult.integration.arrow.toKResult
 *
 * fun test() {
 *     Either.Right("test")
 *         .toKResult()
 *         .isSuccess() shouldBe true
 * }
 * ```
 *
 * <!--- KNIT example-arrow-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 */
fun <A, B> Either<A, B>.toKResult(): KResult<A, B> =
  this.fold(
    { failure -> KResult.Failure(failure) },
    { success -> KResult.Success(success) }
  )

/**
 * Transforms a [KResult] to an Arrow [Either]
 *
 * ```kotlin
 * import io.kotest.matchers.shouldBe
 * import io.kresult.integration.arrow.toEither
 * import io.kresult.core.KResult
 *
 * fun test() {
 *     KResult.Success("test")
 *         .toEither()
 *         .isRight() shouldBe true
 * }
 * ```
 *
 * <!--- KNIT example-arrow-02.kt -->
 * <!--- TEST lines.isEmpty() -->
 */
fun <E, T> KResult<E, T>.toEither(): Either<E, T> =
  this.fold(
    { failure -> Either.Left(failure) },
    { success -> Either.Right(success) }
  )
