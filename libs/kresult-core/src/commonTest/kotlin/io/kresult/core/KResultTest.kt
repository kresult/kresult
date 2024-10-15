package io.kresult.core

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.Success
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class KResultTest {

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
                it.getOrElse { "else" }.shouldBeEqual("success+map")
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

    private fun success(): KResult<Nothing, String> = "success".asSuccess()

    private fun failure(): KResult<String, Nothing> = "failure".asFailure()

    object A
    object B
}