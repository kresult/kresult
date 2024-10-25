package io.kresult.core

import io.kresult.core.KResult.*
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KResultTest {

  /**
   * components & destructuring
   *
   * @see KResult.component1
   * @see KResult.component2
   */

  @Test
  fun `component1 - Success should return null error`() {
    val (error, _) = Success("value")
    error shouldBe null
  }

  @Test
  fun `component1 - Failure should return error`() {
    val (error, _) = Failure("error")
    error shouldBe "error"
  }

  @Test
  fun `component1 - FailureWithValue should return error`() {
    val (error, _) = FailureWithValue("error", "value")
    error shouldBe "error"
  }

  @Test
  fun `component2 - Success should return value`() {
    val (_, value) = Success("value")
    value shouldBe "value"
  }

  @Test
  fun `component2 - Failure should return null value`() {
    val (_, value) = Failure("error")
    value shouldBe null
  }

  @Test
  fun `component2 - FailureWithValue should return value`() {
    val (_, value) = FailureWithValue("error", "value")
    value shouldBe "value"
  }

  @Test
  fun `destructuring - should work with both components`() {
    val (error1, value1) = Success("value")
    error1 shouldBe null
    value1 shouldBe "value"

    val (error2, value2) = Failure("error")
    error2 shouldBe "error"
    value2 shouldBe null

    val (error3, value3) = FailureWithValue("error", "value")
    error3 shouldBe "error"
    value3 shouldBe "value"
  }

  /**
   * isFailure
   *
   * @see KResult.isFailure
   */

  @Test
  fun `isFailure - Success should return false`() {
    Success("value").isFailure() shouldBe false
  }

  @Test
  fun `isFailure - Failure should return true`() {
    Failure("error").isFailure() shouldBe true
  }

  @Test
  fun `isFailure - FailureWithValue should return true`() {
    FailureWithValue("error", "value").isFailure() shouldBe true
  }

  // FIXME
  // @Test
  // fun `isFailure - should enable smart casting to Failure`() {
  //   val result: KResult<String, String> = Failure("error")
  //   if (result.isFailure()) {
  //     result.error shouldBe "error"
  //   }
  // }

  // FIXME
  // @Test
  // fun `isFailure - should enable smart casting to FailureWithValue`() {
  //   val result: KResult<String, String> = FailureWithValue("error", "value")
  //   if (result.isFailure()) {
  //     result.error shouldBe "error"
  //   }
  // }

  /**
   * isSuccess
   *
   * @see KResult.isSuccess
   */

  @Test
  fun `isSuccess - Success should return true`() {
    Success("value").isSuccess() shouldBe true
  }

  @Test
  fun `isSuccess - Failure should return false`() {
    Failure("error").isSuccess() shouldBe false
  }

  @Test
  fun `isSuccess - FailureWithValue should return false`() {
    FailureWithValue("error", "value").isSuccess() shouldBe false
  }

  // FIXME
  // @Test
  // fun `isSuccess - should enable smart casting to Success`() {
  //  val result: KResult<String, String> = Success("value")
  //  if (result.isSuccess()) {
  //    result.value shouldBe "value"
  //  }
  // }

  /**
   * fold
   *
   * @see KResult.fold
   */

  @Test
  fun `fold - Success should transform value`() {
    Success("value").fold(
      ifFailure = { "failure: $it" },
      ifSuccess = { "success: $it" }
    ) shouldBe "success: value"
  }

  @Test
  fun `fold - Failure should transform error`() {
    Failure("error").fold(
      ifFailure = { "failure: $it" },
      ifSuccess = { "success: $it" }
    ) shouldBe "failure: error"
  }

  @Test
  fun `fold - FailureWithValue should transform error`() {
    FailureWithValue("error", "value").fold(
      ifFailure = { "failure: $it" },
      ifSuccess = { "success: $it" }
    ) shouldBe "failure: error"
  }

  @Test
  fun `fold - should allow type transformation`() {
    val success: KResult<String, Int> = Success(42)
    val failure: KResult<String, Int> = Failure("error")

    success.fold(
      ifFailure = { it.length },
      ifSuccess = { it * 2 }
    ) shouldBe 84

    failure.fold(
      ifFailure = { it.length },
      ifSuccess = { it * 2 }
    ) shouldBe 5
  }

  @Test
  fun `fold - should invoke transformation exactly once`() {
    var successCount = 0
    var failureCount = 0

    Success("value").fold(
      ifFailure = {
        failureCount++
        it
      },
      ifSuccess = {
        successCount++
        it
      },
    )

    successCount shouldBe 1
    failureCount shouldBe 0

    successCount = 0
    failureCount = 0

    Failure("error").fold(
      ifFailure = {
        failureCount++
        it
      },
      ifSuccess = {
        successCount++
        it
      },
    )

    successCount shouldBe 0
    failureCount shouldBe 1
  }

  /**
   * map
   *
   * @see KResult.map
   */

  @Test
  fun `map - Success should transform value`() {
    Success(1).map { it + 1 } shouldBe Success(2)
  }

  @Test
  fun `map - Failure should remain unchanged`() {
    Failure("error").map { "unused" } shouldBe Failure("error")
  }

  @Test
  fun `map - FailureWithValue should remain error unchanged`() {
    FailureWithValue("error", 1).map { it + 1 } shouldBe FailureWithValue("error", 1)
  }

  @Test
  fun `map - should allow type transformation`() {
    Success(1).map { it.toString() } shouldBe Success("1")
  }

  @Test
  fun `map - should invoke transformation exactly once`() {
    var count = 0
    Success(1).map {
      count++
      it
    }
    count shouldBe 1
  }

  @Test
  fun `map - on Failure should not invoke transformation`() {
    var count = 0
    Failure("error").map {
      count++
      it
    }
    count shouldBe 0
  }

  @Test
  fun `map - on FailureWithValue should invoke transformation anyways to carry the new value`() {
    var count = 0
    FailureWithValue("error", 1).map {
      count++
      it
    }
    count shouldBe 1
  }

  /**
   * mapFailure
   *
   * @see KResult.mapFailure
   */

  @Test
  fun `mapFailure - Success should remain unchanged`() {
    Success(1).mapFailure { "unused" } shouldBe Success(1)
  }

  @Test
  fun `mapFailure - Failure should transform error`() {
    Failure(1).mapFailure { it + 1 } shouldBe Failure(2)
  }

  @Test
  fun `mapFailure - FailureWithValue should transform error and keep value`() {
    FailureWithValue(1, "value").mapFailure { it + 1 } shouldBe FailureWithValue(2, "value")
  }

  @Test
  fun `mapFailure - should allow type transformation`() {
    Failure(1).mapFailure { it.toString() } shouldBe Failure("1")
  }

  @Test
  fun `mapFailure - should invoke transformation exactly once`() {
    var count = 0
    Failure(1).mapFailure {
      count++
      it
    }
    count shouldBe 1
  }

  @Test
  fun `mapFailure - on Success should not invoke transformation`() {
    var count = 0
    Success(1).mapFailure {
      count++
      it
    }
    count shouldBe 0
  }

  @Test
  fun `mapFailure - on FailureWithValue should invoke transformation exactly once`() {
    var count = 0
    FailureWithValue(1, "value").mapFailure {
      count++
      it
    }
    count shouldBe 1
  }

  /**
   * onSuccess
   *
   * @see KResult.onSuccess
   */

  @Test
  fun `onSuccess - Success should execute action`() {
    var executed = false
    Success(1).onSuccess { executed = true }
    executed shouldBe true
  }

  @Test
  fun `onSuccess - Success should pass correct value to action`() {
    var capturedValue = 0
    Success(42).onSuccess { capturedValue = it }
    capturedValue shouldBe 42
  }

  @Test
  fun `onSuccess - Failure should not execute action`() {
    var executed = false
    Failure("error").onSuccess { executed = true }
    executed shouldBe false
  }

  @Test
  fun `onSuccess - FailureWithValue should not execute action`() {
    var executed = false
    FailureWithValue("error", 1).onSuccess { executed = true }
    executed shouldBe false
  }

  @Test
  fun `onSuccess - should invoke action exactly once`() {
    var count = 0
    Success(1).onSuccess { count++ }
    count shouldBe 1
  }

  @Test
  fun `onSuccess - should maintain original result with Success`() {
    Success(1).onSuccess { it + 1 } shouldBe Success(1)
  }

  @Test
  fun `onSuccess - should maintain original result with Failure`() {
    Failure("error").onSuccess { "changed" } shouldBe Failure("error")
  }

  /**
   * onFailure
   *
   * @see KResult.onFailure
   */

  @Test
  fun `onFailure - Failure should execute action`() {
    var executed = false
    Failure("error").onFailure { executed = true }
    executed shouldBe true
  }

  @Test
  fun `onFailure - Failure should pass correct error to action`() {
    var capturedError = ""
    Failure("error42").onFailure { capturedError = it }
    capturedError shouldBe "error42"
  }

  @Test
  fun `onFailure - Success should not execute action`() {
    var executed = false
    Success(1).onFailure { executed = true }
    executed shouldBe false
  }

  @Test
  fun `onFailure - FailureWithValue should execute action`() {
    var executed = false
    FailureWithValue("error", 1).onFailure { executed = true }
    executed shouldBe true
  }

  @Test
  fun `onFailure - should invoke action exactly once`() {
    var count = 0
    Failure("error").onFailure { count++ }
    count shouldBe 1
  }

  @Test
  fun `onFailure - should maintain original result with Failure`() {
    Failure("error").onFailure { "changed" } shouldBe Failure("error")
  }

  @Test
  fun `onFailure - should maintain original result with Success`() {
    Success(1).onFailure { "changed" } shouldBe Success(1)
  }

  /**
   * getOrNull
   *
   * @see KResult.getOrNull
   */

  @Test
  fun `getOrNull - Success should return value`() {
    Success("value").getOrNull() shouldBe "value"
  }

  @Test
  fun `getOrNull - Failure should return null`() {
    Failure("error").getOrNull() shouldBe null
  }

  @Test
  fun `getOrNull - FailureWithValue should return null`() {
    FailureWithValue("error", "value").getOrNull() shouldBe null
  }

  @Test
  fun `getOrNull - should enable smart casting for Success`() {
    val result: KResult<String, String> = Success("value")
    val value = result.getOrNull()
    if (value != null) {
      value.length shouldBe 5
    }
  }

  /**
   * failureOrNull
   *
   * @see KResult.failureOrNull
   */

  @Test
  fun `failureOrNull - Success should return null`() {
    Success("value").failureOrNull() shouldBe null
  }

  @Test
  fun `failureOrNull - Failure should return error`() {
    Failure("error").failureOrNull() shouldBe "error"
  }

  @Test
  fun `failureOrNull - FailureWithValue should return error`() {
    FailureWithValue("error", "value").failureOrNull() shouldBe "error"
  }

  @Test
  fun `failureOrNull - should enable smart casting for Failure`() {
    val result: KResult<String, String> = Failure("error")
    val error = result.failureOrNull()
    if (error != null) {
      error.length shouldBe 5
    }
  }

  /**
   * swap
   *
   * @see KResult.swap
   */

  @Test
  fun `swap - Success should become Failure`() {
    Success("value").swap() shouldBe Failure("value")
  }

  @Test
  fun `swap - Failure should become Success`() {
    Failure("error").swap() shouldBe Success("error")
  }

  @Test
  fun `swap - FailureWithValue should convert to Success of error`() {
    FailureWithValue("error", "value").swap() shouldBe Success("error")
  }

  @Test
  fun `swap - should allow different types`() {
    val result: KResult<Int, String> = Success("42")
    val swapped: KResult<String, Int> = result.swap()
    swapped shouldBe Failure("42")
  }

  @Test
  fun `swap - should be reversible`() {
    Success("value").swap().swap() shouldBe Success("value")
    Failure("error").swap().swap() shouldBe Failure("error")
  }

  /**
   * catch (companion)
   *
   * @see KResult.Companion.catch
   */

  @Test
  fun `catch - should return Success when no exception is thrown`() {
    KResult.catch { "value" } shouldBe Success("value")
  }

  @Test
  fun `catch - should return Failure when exception is thrown`() {
    val exception = RuntimeException("error")
    KResult.catch { throw exception } shouldBe Failure(exception)
  }

  @Test
  fun `catch - should maintain exception details`() {
    val cause = IllegalStateException("cause")
    val exception = RuntimeException("error", cause)

    val result = KResult.catch { throw exception }

    result.failureOrNull()?.message shouldBe "error"
    result.failureOrNull()?.cause?.message shouldBe "cause"
  }

  @Test
  fun `catch - should capture different exception types`() {
    KResult.catch { throw IllegalArgumentException("error") }
      .isFailure() shouldBe true

    KResult.catch { throw IllegalStateException("error") }
      .isFailure() shouldBe true

    KResult.catch { throw NullPointerException("error") }
      .isFailure() shouldBe true
  }

  @Test
  fun `catch - should handle nested exceptions`() {
    val result = KResult.catch {
      try {
        throw IllegalStateException("inner")
      } catch (e: Exception) {
        throw RuntimeException("outer", e)
      }
    }

    result.failureOrNull()?.message shouldBe "outer"
    result.failureOrNull()?.cause?.message shouldBe "inner"
  }

  @Test
  fun `catch - should allow different return types`() {
    KResult.catch { 42 } shouldBe Success(42)
    KResult.catch { "42" } shouldBe Success("42")
    KResult.catch { listOf(1, 2, 3) } shouldBe Success(listOf(1, 2, 3))
  }

  /**
   * fromNullable (companion)
   *
   * @see KResult.Companion.fromNullable
   */

  @Test
  fun `fromNullable with errFn - should return Success for non-null value`() {
    KResult.fromNullable("value") { "error" } shouldBe Success("value")
  }

  @Test
  fun `fromNullable with errFn - should return Failure with custom error for null`() {
    KResult.fromNullable(null) { "custom error" } shouldBe Failure("custom error")
  }

  @Test
  fun `fromNullable with errFn - should allow error function to use different type`() {
    KResult.fromNullable(null) { 42 } shouldBe Failure(42)
  }

  @Test
  fun `fromNullable without errFn - should return Success for non-null value`() {
    KResult.fromNullable("value") shouldBe Success("value")
  }

  @Test
  fun `fromNullable without errFn - should return Failure with NPE for null`() {
    val result = KResult.fromNullable(null)
    result.isFailure() shouldBe true
    result.failureOrNull()?.message shouldBe "Value null!"
    result.failureOrNull().shouldBeInstanceOf<NullPointerException>()
  }

  @Test
  fun `fromNullable - should work with different value types`() {
    KResult.fromNullable(42) { "error" } shouldBe Success(42)
    KResult.fromNullable(listOf(1, 2, 3)) { "error" } shouldBe Success(listOf(1, 2, 3))
    KResult.fromNullable(mapOf("key" to "value")) { "error" } shouldBe Success(mapOf("key" to "value"))
  }

  /**
   * combine (companion)
   *
   * @see KResult.Companion.combine
   */

  @Test
  fun `combine - Success + Success should combine values`() {
    KResult.combine<String, Int>(
      Success(1),
      Success(2),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe Success(3)
  }

  @Test
  fun `combine - Success + Failure should return second Failure`() {
    KResult.combine(
      Success(1),
      Failure("error"),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe Failure("error")
  }

  @Test
  fun `combine - Success + FailureWithValue should return second's error`() {
    KResult.combine(
      Success(1),
      FailureWithValue("error", 2),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe FailureWithValue("error", 2)
  }

  @Test
  fun `combine - Failure + Success should return first Failure`() {
    KResult.combine(
      Failure("error"),
      Success(1),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe Failure("error")
  }

  @Test
  fun `combine - Failure + Failure should combine errors`() {
    KResult.combine<String, Int>(
      Failure("error1"),
      Failure("error2"),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe Failure("error1 + error2")
  }

  @Test
  fun `combine - Failure + FailureWithValue should combine errors`() {
    KResult.combine(
      Failure("error1"),
      FailureWithValue("error2", 1),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe Failure("error1 + error2")
  }

  @Test
  fun `combine - FailureWithValue + Success should return first error`() {
    KResult.combine(
      FailureWithValue("error", 1),
      Success(2),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe FailureWithValue("error", 1)
  }

  @Test
  fun `combine - FailureWithValue + Failure should combine errors`() {
    KResult.combine(
      FailureWithValue("error1", 1),
      Failure("error2"),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe Failure("error1 + error2")
  }

  @Test
  fun `combine - FailureWithValue + FailureWithValue should combine errors`() {
    KResult.combine(
      FailureWithValue("error1", 1),
      FailureWithValue("error2", 2),
      combineFailure = { e1, e2 -> "$e1 + $e2" },
      combineSuccess = { v1, v2 -> v1 + v2 }
    ) shouldBe Failure("error1 + error2")
  }

  @Test
  fun `KSuccess returns isSuccess == true and isFailure == false`() {
    success().isSuccess().shouldBeTrue()
    success().isFailure().shouldBeFalse()
  }

  @Test
  fun `KFailure returns isSuccess == false and isFailure == true`() {
    failure().isSuccess().shouldBeFalse()
    failure().isFailure().shouldBeTrue()
  }

  @Test
  @Suppress("UNREACHABLE_CODE")
  fun `can map to other success value`() {
    success()
      .map { "$it+map" }
      .let {
        it.shouldBeInstanceOf<Success<String>>()
        it.getOrDefault { "else" }.shouldBeEqual("success+map")
      }

    failure()
      .map { "foo" }
      .let {
        it.shouldBeInstanceOf<Failure<String>>()
        it.failureOrNull()!!.shouldBeEqual("failure")
      }
  }

  @Test
  @Suppress("UNREACHABLE_CODE")
  fun `can mapFailure to other failure value`() {
    failure()
      .mapFailure { "$it+map" }
      .let {
        it.shouldBeInstanceOf<Failure<String>>()
        it.failureOrNull()!!.shouldBeEqual("failure+map")
      }

    success()
      .mapFailure { "foo" }
      .let {
        it.shouldBeInstanceOf<Success<String>>()
        it.getOrNull()!!.shouldBeEqual("success")
      }
  }

  @Test
  fun `can execute a side effect onSuccess`() {
    var res = "none"
    success()
      .onSuccess {
        res = "onSuccess"
      }
      .onFailure {
        res = "onFailure"
      }
    res.shouldBeEqual("onSuccess")
  }

  @Test
  fun `can execute a side effect onFailure`() {
    var res = "none"
    failure()
      .onSuccess {
        res = "onSuccess"
      }
      .onFailure {
        res = "onFailure"
      }
    res.shouldBeEqual("onFailure")
  }

  @Test
  fun `can return success or null`() {
    assertNotNull(success().getOrNull())
    assertNull(failure().getOrNull())
  }

  @Test
  fun `can return failure or null`() {
    assertNotNull(failure().failureOrNull())
    assertNull(success().failureOrNull())
  }

  @Test
  fun `can swap success and failure objects`() {
    A.asSuccess().swap().shouldBeInstanceOf<Failure<A>>()
    A.asFailure().swap().shouldBeInstanceOf<Success<A>>()
  }

  @Test
  fun `can flatMap a success and failure`() {
    assertSoftly {
      // Success case
      val initialSuccess: KResult<String, Int> = Success(5)

      val mappedSuccess = initialSuccess.flatMap { value ->
        Success(value * 2)
      }

      mappedSuccess.shouldBeInstanceOf<Success<Int>>()
      mappedSuccess.getOrNull() shouldBe 10

      // Failure case
      val initialFailure: KResult<String, Int> = Failure("Error")

      val mappedFailure = initialFailure.flatMap { value ->
        Success(value * 2)
      }

      mappedFailure.shouldBeInstanceOf<Failure<String>>()
      mappedFailure.failureOrNull() shouldBe "Error"

      // Success to Failure case
      val successToFailure = initialSuccess.flatMap { value ->
        Failure<String>("Transformed to failure")
      }

      successToFailure.shouldBeInstanceOf<Failure<String>>()
      successToFailure.failureOrNull() shouldBe "Transformed to failure"
    }
  }

  @Test
  fun `can flatMapFailure a success and failure`() {
    assertSoftly {
      // Failure case
      val initialFailure: KResult<String, Int> = Failure("Error")

      val mappedFailure = initialFailure.flatMapFailure { error ->
        Failure("Transformed: $error")
      }

      mappedFailure.shouldBeInstanceOf<Failure<String>>()
      mappedFailure.failureOrNull() shouldBe "Transformed: Error"

      // Success case
      val initialSuccess: KResult<String, Int> = Success(5)

      val mappedSuccess = initialSuccess.flatMapFailure { error ->
        Failure("This should not happen")
      }

      mappedSuccess.shouldBeInstanceOf<Success<Int>>()
      mappedSuccess.getOrNull() shouldBe 5

      // Failure to Success case
      val failureToSuccess = initialFailure.flatMapFailure { error ->
        Success(42)
      }

      failureToSuccess.shouldBeInstanceOf<Success<Int>>()
      failureToSuccess.getOrNull() shouldBe 42
    }
  }

  @Test
  fun `can flatten nested KResults`() {
    assertSoftly {
      // Success case

      // Nested Success case
      val nestedSuccess: KResult<String, KResult<String, Int>> = Success(Success(5))

      val flattenedSuccess = nestedSuccess.flatten()

      flattenedSuccess.shouldBeInstanceOf<Success<Int>>()
      flattenedSuccess.getOrNull() shouldBe 5

      // Failure cases

      // Outer Failure case
      val outerFailure: KResult<String, KResult<String, Int>> = Failure("Outer Error")

      val flattenedOuterFailure = outerFailure.flatten()

      flattenedOuterFailure.shouldBeInstanceOf<Failure<String>>()
      flattenedOuterFailure.failureOrNull() shouldBe "Outer Error"

      // Inner Failure case
      val innerFailure: KResult<String, KResult<String, Int>> = Success(Failure("Inner Error"))

      val flattenedInnerFailure = innerFailure.flatten()

      flattenedInnerFailure.shouldBeInstanceOf<Failure<String>>()
      flattenedInnerFailure.failureOrNull() shouldBe "Inner Error"
    }
  }

  @Test
  fun `can flattenFailure nested KResults`() {
    assertSoftly {
      // Failure cases

      // Nested Failure case
      val nestedFailure: KResult<KResult<String, Int>, Int> = Failure(Failure("Nested Error"))

      val flattenedNestedFailure = nestedFailure.flattenFailure()

      flattenedNestedFailure.shouldBeInstanceOf<Failure<String>>()
      flattenedNestedFailure.failureOrNull() shouldBe "Nested Error"

      // Simple Failure case
      val simpleFailure: KResult<KResult<String, Int>, Int> = Failure(Success(5))

      val flattenedSimpleFailure = simpleFailure.flattenFailure()

      flattenedSimpleFailure.shouldBeInstanceOf<Success<Int>>()
      flattenedSimpleFailure.getOrNull() shouldBe 5

      // Success case

      // Success remains unchanged
      val success: KResult<KResult<String, Int>, Int> = Success(10)

      val flattenedSuccess = success.flattenFailure()

      flattenedSuccess.shouldBeInstanceOf<Success<Int>>()
      flattenedSuccess.getOrNull() shouldBe 10

      // Note: Unlike `flatten`, `flattenFailure` is defined for KResult<KResult<E, T>, T>,
      // where the outer result is potentially a failure containing another result.
    }
  }

  @Test
  fun `KResult_catch can create KResult by catching an Exception`() {
    val e = KResult.catch {
      "test"
    }

    e.shouldBeInstanceOf<KResult.Success<String>>()
    e.value.shouldBeEqual("test")

    val f = KResult.catch {
      throw RuntimeException("ex")
    }

    f.shouldBeInstanceOf<KResult.Failure<Throwable>>()
    f.error.message!!.shouldBeEqual("ex")
  }

  private fun success(): KResult<Nothing, String> = "success".asSuccess()

  private fun failure(): KResult<String, Nothing> = "failure".asFailure()

  object A
  object B
}
