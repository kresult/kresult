package io.kresult.core

import io.kresult.core.KResult.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class KResultExtensionsTest {

  /**
   * flatMap
   *
   * @see flatMap
   */

  @Test
  fun `flatMap - from Success to Success should result in Success`() {
    Success(1)
      .flatMap { Success(it + 1) } shouldBe Success(2)
  }

  @Test
  fun `flatMap - from Success to Failure should result in Failure`() {
    Success(1)
      .flatMap { Failure("error") } shouldBe Failure("error")
  }

  @Test
  fun `flatMap - from Failure should maintain the original error`() {
    Failure("original error")
      .flatMap { Success(42) } shouldBe Failure("original error")
  }

  @Test
  fun `flatMap - from FailureWithValue to Success should maintain original error with new value`() {
    FailureWithValue("error", 1)
      .flatMap { Success(it + 1) } shouldBe FailureWithValue("error", 2)
  }

  @Test
  fun `flatMap - from FailureWithValue to Failure should result in Failure with original error`() {
    FailureWithValue("original error", 1)
      .flatMap { Failure("new error") } shouldBe Failure("original error")
  }

  @Test
  fun `flatMap - should handle complex transformations`() {
    Success(1)
      .flatMap { Success(it + 1) }
      .flatMap { Success(it * 2) }
      .flatMap { Success(it.toString()) } shouldBe Success("4")
  }

  @Test
  fun `flatMap - chain should short-circuit on first Failure`() {
    Success(1)
      .flatMap { Success(it + 1) }
      .flatMap { Failure("error") }
      .flatMap { Success(42) } shouldBe Failure("error")
  }

  @Test
  fun `flatMap - with FailureWithValue chain should preserve error context`() {
    FailureWithValue("validation error", 1)
      .flatMap { Success(it + 1) }
      .flatMap { Success(it * 2) } shouldBe FailureWithValue("validation error", 4)
  }

  /**
   * flatMapFailure
   *
   * @see flatMapFailure
   */

  @Test
  fun `flatMapFailure - from Failure to Success should result in Success`() {
    Failure("error")
      .flatMapFailure { Success(42) } shouldBe Success(42)
  }

  @Test
  fun `flatMapFailure - from Failure to Failure should result in new Failure`() {
    Failure("original error")
      .flatMapFailure { Failure("new error") } shouldBe Failure("new error")
  }

  @Test
  fun `flatMapFailure - from Success should maintain original value`() {
    Success(42)
      .flatMapFailure { Success(100) } shouldBe Success(42)
  }

  @Test
  fun `flatMapFailure - from FailureWithValue to Success should result in Success with original value`() {
    FailureWithValue("error", 42)
      .flatMapFailure { Success(100) } shouldBe Success(42)
  }

  @Test
  fun `flatMapFailure - from FailureWithValue to Failure should maintain original value`() {
    FailureWithValue("original error", 42)
      .flatMapFailure { Failure("new error") } shouldBe FailureWithValue("new error", 42)
  }

  @Test
  fun `flatMapFailure - should handle complex transformations`() {
    Failure("error-1")
      .flatMapFailure { Success(42) }
      .flatMapFailure { Failure("never-called") } shouldBe Success(42)
  }

  @Test
  fun `flatMapFailure - chain should stop mapping after first Success`() {
    Failure("error-1")
      .flatMapFailure { Failure("error-2") }
      .flatMapFailure { Success(42) } shouldBe Success(42)
  }

  @Test
  fun `flatMapFailure - with FailureWithValue chain should handle transitions`() {
    FailureWithValue("error-1", 42)
      .flatMapFailure { Failure("error-2") }
      .flatMapFailure { Success(100) } shouldBe Success(42)
  }

  /**
   * flatten
   *
   * @see flatten
   */

  @Test
  fun `flatten - nested Success should unwrap to single Success`() {
    Success(Success(42)).flatten() shouldBe Success(42)
  }

  @Test
  fun `flatten - outer Failure should propagate error`() {
    val result: KResult<String, KResult<String, Int>> = Failure("error")
    result.flatten() shouldBe Failure("error")
  }

  @Test
  fun `flatten - Success with inner Failure should result in Failure`() {
    Success(Failure("error")).flatten() shouldBe Failure("error")
  }

  @Test
  fun `flatten - outer FailureWithValue should convert to Failure`() {
    FailureWithValue("error", Success(42)).flatten() shouldBe FailureWithValue("error", Success(42))
  }

  @Test
  fun `flatten - Success with inner FailureWithValue should convert to Failure`() {
    Success(FailureWithValue("error", 42)).flatten() shouldBe FailureWithValue("error", 42)
  }

  @Test
  fun `flatten - should flatten only one level of nesting`() {
    Success(Success(Success(42))).flatten() shouldBe Success(Success(42))
  }

  /**
   * flattenFailure
   *
   * @see flattenFailure
   */

  @Test
  fun `flattenFailure - Success should remain unchanged`() {
    val result: KResult<KResult<String, Int>, Int> = Success(42)
    result.flattenFailure() shouldBe Success(42)
  }

  @Test
  fun `flattenFailure - nested Failure should unwrap to single Failure`() {
    Failure(Failure("error")).flattenFailure() shouldBe Failure("error")
  }

  @Test
  fun `flattenFailure - Failure with Success should result in Success`() {
    Failure(Success(42)).flattenFailure() shouldBe Success(42)
  }

  @Test
  fun `flattenFailure - Failure with FailureWithValue should result in FailureWithValue`() {
    Failure(FailureWithValue("error", 42)).flattenFailure() shouldBe FailureWithValue("error", 42)
  }

  @Test
  fun `flattenFailure - should flatten only one level of nested Failure`() {
    val innermost: KResult<String, Int> = Failure("error")
    val middle: KResult<KResult<String, Int>, Int> = Failure(innermost)
    val outer: KResult<KResult<KResult<String, Int>, Int>, Int> = Failure(middle)

    outer.flattenFailure() shouldBe Failure(innermost)
  }

  /**
   * filter
   *
   * @see filter
   */

  @Test
  fun `filter - Success passing predicate should remain unchanged`() {
    Success(42)
      .filter(
        failureFn = { "should not be called" },
        f = { it > 0 }
      ) shouldBe Success(42)
  }

  @Test
  fun `filter - Success failing predicate should convert to Failure`() {
    Success(42)
      .filter(
        failureFn = { "number too large" },
        f = { it < 40 }
      ) shouldBe Failure("number too large")
  }

  @Test
  fun `filter - Failure should remain unchanged`() {
    Failure("original error")
      .filter(
        failureFn = { "should not be called" },
        f = { true }
      ) shouldBe Failure("original error")
  }

  @Test
  fun `filter - FailureWithValue should convert to Failure`() {
    FailureWithValue("original error", 42)
      .filter(
        failureFn = { "should not be called" },
        f = { true }
      ) shouldBe Failure("original error")
  }

  @Test
  fun `filter - should work with complex predicate`() {
    Success("test@email.com")
      .filter(
        failureFn = { "invalid email" },
        f = { it.contains("@") && it.contains(".") }
      ) shouldBe Success("test@email.com")
  }

  @Test
  fun `filter - can be chained`() {
    Success(42)
      .filter(
        failureFn = { "not positive" },
        f = { it > 0 }
      )
      .filter(
        failureFn = { "too large" },
        f = { it < 100 }
      ) shouldBe Success(42)
  }

  @Test
  fun `filter - chain should short-circuit on first failure`() {
    Success(42)
      .filter(
        failureFn = { "too large" },
        f = { it < 40 }
      )
      .filter(
        failureFn = { "should not be called" },
        f = { true }
      ) shouldBe Failure("too large")
  }

  /**
   * filter with value (overloaded with fitlerValue instead of filterFn)
   *
   * @see filter
   */

  @Test
  fun `filter with value - Success passing predicate should remain unchanged`() {
    Success(42)
      .filter(
        failureValue = "should not be used",
        f = { it > 0 }
      ) shouldBe Success(42)
  }

  @Test
  fun `filter with value - Success failing predicate should convert to Failure`() {
    Success(42)
      .filter(
        failureValue = "number too large",
        f = { it < 40 }
      ) shouldBe Failure("number too large")
  }

  @Test
  fun `filter with value - Failure should remain unchanged`() {
    Failure("original error")
      .filter(
        failureValue = "should not be used",
        f = { true }
      ) shouldBe Failure("original error")
  }

  @Test
  fun `filter with value - FailureWithValue should convert to Failure`() {
    FailureWithValue("original error", 42)
      .filter(
        failureValue = "should not be used",
        f = { true }
      ) shouldBe Failure("original error")
  }

  @Test
  fun `filter with value - should work with complex predicate`() {
    Success("test@email.com")
      .filter(
        failureValue = "invalid email",
        f = { it.contains("@") && it.contains(".") }
      ) shouldBe Success("test@email.com")
  }

  @Test
  fun `filter with value - can be chained`() {
    Success(42)
      .filter(
        failureValue = "not positive",
        f = { it > 0 }
      )
      .filter(
        failureValue = "too large",
        f = { it < 100 }
      ) shouldBe Success(42)
  }

  @Test
  fun `filter with value - chain should short-circuit on first failure`() {
    Success(42)
      .filter(
        failureValue = "too large",
        f = { it < 40 }
      )
      .filter(
        failureValue = "should not be used",
        f = { true }
      ) shouldBe Failure("too large")
  }

  /**
   * getOrDefault
   *
   * @see getOrDefault
   */

  @Test
  fun `getOrDefault - Success should return its value`() {
    Success(42) getOrDefault { 100 } shouldBe 42
  }

  @Test
  fun `getOrDefault - Failure should return default value using error`() {
    Failure("error") getOrDefault { error -> "$error transformed" } shouldBe "error transformed"
  }

  @Test
  fun `getOrDefault - FailureWithValue should return default value using error`() {
    FailureWithValue("error", 42) getOrDefault { error -> "$error transformed" } shouldBe "error transformed"
  }

  @Test
  fun `getOrDefault - default function should receive original error`() {
    var capturedError = ""
    Failure("original error") getOrDefault { error ->
      capturedError = error
      42
    }
    capturedError shouldBe "original error"
  }

  @Test
  fun `getOrDefault - should work with same type for value and default`() {
    Success(42) getOrDefault { 100 } shouldBe 42
    Failure("error") getOrDefault { 100 } shouldBe 100
    FailureWithValue("error", 42) getOrDefault { 100 } shouldBe 100
  }

  @Test
  fun `getOrDefault - should allow type transformation in default function`() {
    val success: KResult<String, Int> = Success(42)
    val failure: KResult<String, Int> = Failure("error")
    val failureWithValue: KResult<String, Int> = FailureWithValue("error", 42)

    success getOrDefault { it.length } shouldBe 42
    failure getOrDefault { it.length } shouldBe 5
    failureWithValue getOrDefault { it.length } shouldBe 5
  }

  /**
   * failureOrDefault
   *
   * @see failureOrDefault
   */

  @Test
  fun `failureOrDefault - Failure should return its error`() {
    Failure("error") failureOrDefault { "unused" } shouldBe "error"
  }

  @Test
  fun `failureOrDefault - Success should return default error using value`() {
    Success(42) failureOrDefault { value -> "value was $value" } shouldBe "value was 42"
  }

  @Test
  fun `failureOrDefault - FailureWithValue should return its error`() {
    FailureWithValue("error", 42) failureOrDefault { "unused" } shouldBe "error"
  }

  @Test
  fun `failureOrDefault - default function should receive original value`() {
    var capturedValue = 0
    Success(42) failureOrDefault { value ->
      capturedValue = value
      "error"
    }
    capturedValue shouldBe 42
  }

  @Test
  fun `failureOrDefault - should work with same type for error and default`() {
    Success(42) failureOrDefault { 100 } shouldBe 100
    Failure(100) failureOrDefault { 200 } shouldBe 100
    FailureWithValue(100, 42) failureOrDefault { 200 } shouldBe 100
  }

  @Test
  fun `failureOrDefault - should allow type transformation in default function`() {
    val success: KResult<Int, String> = Success("error")
    val failure: KResult<Int, String> = Failure(42)
    val failureWithValue: KResult<Int, String> = FailureWithValue(42, "error")

    success failureOrDefault { it.length } shouldBe 5
    failure failureOrDefault { it.length } shouldBe 42
    failureWithValue failureOrDefault { it.length } shouldBe 42
  }

  /**
   * getOrThrow
   *
   * @see getOrThrow
   */

  @Test
  fun `getOrThrow - Success should return its value`() {
    Success(42).getOrThrow() shouldBe 42
  }

  @Test
  @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
  fun `getOrThrow - Failure should throw its error`() {
    val exception = RuntimeException("error")
    shouldThrow<RuntimeException> {
      Failure(exception).getOrThrow()
    }.message shouldBe "error"
  }

  @Test
  fun `getOrThrow - FailureWithValue should throw its error`() {
    val exception = RuntimeException("error")
    shouldThrow<RuntimeException> {
      FailureWithValue(exception, 42).getOrThrow()
    }.message shouldBe "error"
  }

  @Test
  @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
  fun `getOrThrow - should preserve exception type`() {
    shouldThrow<IllegalArgumentException> {
      Failure(IllegalArgumentException("invalid")).getOrThrow()
    }

    shouldThrow<IllegalStateException> {
      Failure(IllegalStateException("bad state")).getOrThrow()
    }
  }

  @Test
  @Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")
  fun `getOrThrow - should preserve exception details`() {
    val exception = IllegalStateException("outer", RuntimeException("inner"))
    val thrown = shouldThrow<IllegalStateException> {
      Failure(exception).getOrThrow()
    }
    thrown.message shouldBe "outer"
    thrown.cause?.message shouldBe "inner"
  }

  /**
   * merge
   *
   * @see merge
   */

  @Test
  fun `merge - Success should return its value`() {
    val result: KResult<String, String> = Success("success")
    result.merge() shouldBe "success"
  }

  @Test
  fun `merge - Failure should return its error`() {
    val result: KResult<String, String> = Failure("error")
    result.merge() shouldBe "error"
  }

  @Test
  fun `merge - FailureWithValue should return its error`() {
    val result: KResult<String, String> = FailureWithValue("error", "value")
    result.merge() shouldBe "error"
  }

  /**
   * combine
   *
   * @see combine
   */

  private fun combineErrors(e1: String, e2: String) = "$e1 + $e2"
  private fun combineValues(v1: Int, v2: Int) = v1 + v2

  @Test
  fun `combine - Success + Success should combine values`() {
    Success(1).combine(Success(2), ::combineErrors, ::combineValues) shouldBe Success(3)
  }

  @Test
  fun `combine - Success + Failure should return Failure`() {
    Success(1).combine(Failure("error"), ::combineErrors, ::combineValues) shouldBe Failure("error")
  }

  @Test
  fun `combine - Success + FailureWithValue should return FailureWithValue's error`() {
    val result = Success(1).combine(FailureWithValue("error", 2), ::combineErrors, ::combineValues)
    result shouldBe FailureWithValue("error", 2)
  }

  @Test
  fun `combine - Failure + Success should return first Failure`() {
    Failure("error").combine(Success(1), ::combineErrors, ::combineValues) shouldBe Failure("error")
  }

  @Test
  fun `combine - Failure + Failure should combine errors`() {
    val result = Failure("error1").combine(Failure("error2"), ::combineErrors, ::combineValues)
    result shouldBe Failure("error1 + error2")
  }

  @Test
  fun `combine - Failure + FailureWithValue should combine errors`() {
    val result = Failure("error1").combine(FailureWithValue("error2", 1), ::combineErrors, ::combineValues)
    result shouldBe Failure("error1 + error2")
  }

  @Test
  fun `combine - FailureWithValue + Success should return first error`() {
    val result = FailureWithValue("error", 1).combine(Success(2), ::combineErrors, ::combineValues)
    result shouldBe FailureWithValue("error", 1)
  }

  @Test
  fun `combine - FailureWithValue + Failure should combine errors`() {
    val result = FailureWithValue("error1", 1).combine(Failure("error2"), ::combineErrors, ::combineValues)
    result shouldBe Failure("error1 + error2")
  }

  @Test
  fun `combine - FailureWithValue + FailureWithValue should combine errors`() {
    val first = FailureWithValue("error1", 1)
    val second = FailureWithValue("error2", 2)
    first.combine(second, ::combineErrors, ::combineValues) shouldBe Failure("error1 + error2")
  }
}