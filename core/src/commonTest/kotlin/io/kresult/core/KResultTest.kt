package io.kresult.core

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
                it.shouldBeInstanceOf<KSuccess<String>>()
                it.getOrElse { "else" }.shouldBeEqual("success+map")
            }

        failure()
            .map { "foo" }
            .let {
                it.shouldBeInstanceOf<KFailure<String>>()
                it.failureOrNull()!!.shouldBeEqual("failure")
            }
    }

    @Test
    @Suppress("UNREACHABLE_CODE")
    fun `can mapFailure to other failure value`() {
        failure()
            .mapFailure { "$it+map" }
            .let {
                it.shouldBeInstanceOf<KFailure<String>>()
                it.failureOrNull()!!.shouldBeEqual("failure+map")
            }

        success()
            .mapFailure { "foo" }
            .let {
                it.shouldBeInstanceOf<KSuccess<String>>()
                it.successOrNull()!!.shouldBeEqual("success")
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
        assertNotNull(success().successOrNull())
        assertNull(failure().successOrNull())
    }

    @Test
    fun `can return failure or null`() {
        assertNotNull(failure().failureOrNull())
        assertNull(success().failureOrNull())
    }

    @Test
    fun `can swap success and failure objects`() {
        A.asSuccess().swap().shouldBeInstanceOf<KFailure<A>>()
        A.asFailure().swap().shouldBeInstanceOf<KSuccess<A>>()
    }

    @Test
    fun `can flatMap a success and failure`() {
        assertSoftly {
            // Success case
            val initialSuccess: KResult<String, Int> = KSuccess(5)

            val mappedSuccess = initialSuccess.flatMap { value ->
                KSuccess(value * 2)
            }

            mappedSuccess.shouldBeInstanceOf<KSuccess<Int>>()
            mappedSuccess.successOrNull() shouldBe 10

            // Failure case
            val initialFailure: KResult<String, Int> = KFailure("Error")

            val mappedFailure = initialFailure.flatMap { value ->
                KSuccess(value * 2)
            }

            mappedFailure.shouldBeInstanceOf<KFailure<String>>()
            mappedFailure.failureOrNull() shouldBe "Error"

            // Success to Failure case
            val successToFailure = initialSuccess.flatMap { value ->
                KFailure<String>("Transformed to failure")
            }

            successToFailure.shouldBeInstanceOf<KFailure<String>>()
            successToFailure.failureOrNull() shouldBe "Transformed to failure"
        }
    }

    @Test
    fun `can flatMapFailure a success and failure`() {
        assertSoftly {
            // Failure case
            val initialFailure: KResult<String, Int> = KFailure("Error")

            val mappedFailure = initialFailure.flatMapFailure { error ->
                KFailure("Transformed: $error")
            }

            mappedFailure.shouldBeInstanceOf<KFailure<String>>()
            mappedFailure.failureOrNull() shouldBe "Transformed: Error"

            // Success case
            val initialSuccess: KResult<String, Int> = KSuccess(5)

            val mappedSuccess = initialSuccess.flatMapFailure { error ->
                KFailure("This should not happen")
            }

            mappedSuccess.shouldBeInstanceOf<KSuccess<Int>>()
            mappedSuccess.successOrNull() shouldBe 5

            // Failure to Success case
            val failureToSuccess = initialFailure.flatMapFailure { error ->
                KSuccess(42)
            }

            failureToSuccess.shouldBeInstanceOf<KSuccess<Int>>()
            failureToSuccess.successOrNull() shouldBe 42
        }
    }

    @Test
    fun `can flatten nested KResults`() {
        assertSoftly {
            // Success case

            // Nested Success case
            val nestedSuccess: KResult<String, KResult<String, Int>> = KSuccess(KSuccess(5))

            val flattenedSuccess = nestedSuccess.flatten()

            flattenedSuccess.shouldBeInstanceOf<KSuccess<Int>>()
            flattenedSuccess.successOrNull() shouldBe 5

            // Failure cases

            // Outer Failure case
            val outerFailure: KResult<String, KResult<String, Int>> = KFailure("Outer Error")

            val flattenedOuterFailure = outerFailure.flatten()

            flattenedOuterFailure.shouldBeInstanceOf<KFailure<String>>()
            flattenedOuterFailure.failureOrNull() shouldBe "Outer Error"

            // Inner Failure case
            val innerFailure: KResult<String, KResult<String, Int>> = KSuccess(KFailure("Inner Error"))

            val flattenedInnerFailure = innerFailure.flatten()

            flattenedInnerFailure.shouldBeInstanceOf<KFailure<String>>()
            flattenedInnerFailure.failureOrNull() shouldBe "Inner Error"
        }
    }

    @Test
    fun `can flattenFailure nested KResults`() {
        assertSoftly {
            // Failure cases

            // Nested Failure case
            val nestedFailure: KResult<KResult<String, Int>, Int> = KFailure(KFailure("Nested Error"))

            val flattenedNestedFailure = nestedFailure.flattenFailure()

            flattenedNestedFailure.shouldBeInstanceOf<KFailure<String>>()
            flattenedNestedFailure.failureOrNull() shouldBe "Nested Error"

            // Simple Failure case
            val simpleFailure: KResult<KResult<String, Int>, Int> = KFailure(KSuccess(5))

            val flattenedSimpleFailure = simpleFailure.flattenFailure()

            flattenedSimpleFailure.shouldBeInstanceOf<KSuccess<Int>>()
            flattenedSimpleFailure.successOrNull() shouldBe 5

            // Success case

            // Success remains unchanged
            val success: KResult<KResult<String, Int>, Int> = KSuccess(10)

            val flattenedSuccess = success.flattenFailure()

            flattenedSuccess.shouldBeInstanceOf<KSuccess<Int>>()
            flattenedSuccess.successOrNull() shouldBe 10

            // Note: Unlike `flatten`, `flattenFailure` is defined for KResult<KResult<E, T>, T>,
            // where the outer result is potentially a failure containing another result.
        }
    }

    private fun success(): KResult<Nothing, String> = "success".asSuccess()

    private fun failure(): KResult<String, Nothing> = "failure".asFailure()

    object A
    object B
}