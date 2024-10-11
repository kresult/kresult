package io.kresult.core

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class KResultTest {

    @Test
    fun `can create success using asSuccess`() {
        "test".asSuccess().shouldBeInstanceOf<KSuccess<String>>()
    }

    @Test
    fun `can create failure using asFailure`() {
        "test".asFailure().shouldBeInstanceOf<KFailure<String>>()
    }

    @Test
    fun `can create success of Unit`() {
        KSuccess.unit.shouldBeInstanceOf<KSuccess<Unit>>()
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
                it.getOrNull()!!.shouldBeEqual("success")
            }
    }

    //TODO: flatMap & flatMapFailure

    @Test
    fun `can swap success and failure objects`() {
        A.asSuccess().swap().shouldBeInstanceOf<KFailure<A>>()
        A.asFailure().swap().shouldBeInstanceOf<KSuccess<A>>()
    }

    private fun success(): KResult<Nothing, String> = "success".asSuccess()

    private fun failure(): KResult<String, Nothing> = "failure".asFailure()

    object A
    object B
}