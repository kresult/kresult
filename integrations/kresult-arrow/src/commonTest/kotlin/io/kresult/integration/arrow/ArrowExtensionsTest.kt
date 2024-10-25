package io.kresult.integration.arrow

import io.kresult.core.KResult
import io.kresult.core.KResult.*
import arrow.core.Either
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ArrowExtensionsTest {
  /**
   * Either
   *
   * @see toKResult
   * @see toEither
   */

  @Test
  fun `toKResult - Either Right should convert to Success`() {
    Either.Right("value").toKResult() shouldBe Success("value")
  }

  @Test
  fun `toKResult - Either Left should convert to Failure`() {
    Either.Left("error").toKResult() shouldBe Failure("error")
  }

  @Test
  fun `toKResult - should preserve types`() {
    val either: Either<Int, String> = Either.Right("value")
    val result: KResult<Int, String> = either.toKResult()
    result shouldBe Success("value")
  }

  @Test
  fun `toEither - Success should convert to Right`() {
    Success("value").toEither() shouldBe Either.Right("value")
  }

  @Test
  fun `toEither - Failure should convert to Left`() {
    Failure("error").toEither() shouldBe Either.Left("error")
  }

  @Test
  fun `toEither - FailureWithValue should convert to Left`() {
    FailureWithValue("error", "value").toEither() shouldBe Either.Left("error")
  }

  @Test
  fun `toEither - should preserve types`() {
    val result: KResult<Int, String> = Success("value")
    val either: Either<Int, String> = result.toEither()
    either shouldBe Either.Right("value")
  }

  @Test
  fun `conversion - should be reversible for Success`() {
    val original = Success("value")
    original.toEither().toKResult() shouldBe original
  }

  @Test
  fun `conversion - should be reversible for Failure`() {
    val original = Failure("error")
    original.toEither().toKResult() shouldBe original
  }
}
