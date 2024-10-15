package io.kresult.arrow

import arrow.core.Either
import io.kresult.core.KResult

/**
 * <!--- TEST_NAME ArrowKnitTest -->
 */

/**
 * Transforms an Arrow [Either] to a [KResult]
 *
 * ```kotlin
 * import arrow.core.Either
 * import io.kotest.matchers.shouldBe
 * import io.kresult.arrow.toKResult
 *
 * fun test() {
 *     Either.Right("test")
 *         .toKResult()
 *         .isSuccess() shouldBe true
 * }
 * ```
 *
 * <!--- KNIT example-arrow-01.kt -->
 * <!-- TEST -->
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
 * import io.kresult.arrow.toEither
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
 * <!-- TEST -->
 */
fun <E, T> KResult<E, T>.toEither(): Either<E, T> =
    this.fold(
        { failure -> Either.Left(failure) },
        { success -> Either.Right(success) }
    )
