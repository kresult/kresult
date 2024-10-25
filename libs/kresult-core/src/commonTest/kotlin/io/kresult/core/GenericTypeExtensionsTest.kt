package io.kresult.core

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class GenericTypeExtensionsTest {

  @Test
  fun `Result_asKResult creates a KResult with Throwable on Failure side`() {
    Result.success("test").asKResult() shouldBe KResult.Success("test")
    Result.failure<Unit>(RuntimeException("error")).asKResult().shouldBeInstanceOf<KResult.Failure<RuntimeException>>()
  }

  @Test
  fun `KResult_asResult with Throwable on Failure side creates a Kotlin Result`() {
    KResult.Success("test").asResult() shouldBe Result.success("test")
    KResult.Failure(RuntimeException("error")).asResult().shouldBeInstanceOf<Result<String>>()
    KResult.Failure(RuntimeException("error")).asResult().isFailure shouldBe true
  }

  @Test
  fun `Any_asFailure creates a Failure`() {
    "test".asFailure().shouldBeInstanceOf<KResult.Failure<String>>()
  }

  @Test
  fun `Any_asFailureList creates a Failure with a list type`() {
    "test".asFailureList().shouldBeInstanceOf<KResult.Failure<List<String>>>()
  }

  @Test
  fun `Any_asSuccess creates a Success`() {
    "test".asSuccess().shouldBeInstanceOf<KResult.Success<String>>()
  }

  @Test
  fun `Any_asSuccess creates a Success with a list type`() {
    "test".asSuccessList().shouldBeInstanceOf<KResult.Success<List<String>>>()
  }
}
