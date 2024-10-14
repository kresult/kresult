package io.kresult.core

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kresult.core.KResult.*
import kotlin.test.Test

class KResultExtensionsTest {

    @Test
    fun `can create success using asSuccess`() {
        "test".asSuccess().shouldBeInstanceOf<Success<String>>()
    }

    @Test
    fun `can create failure using asFailure`() {
        "test".asFailure().shouldBeInstanceOf<Failure<String>>()
    }

    @Test
    fun `can create success of Unit`() {
        Success.unit.shouldBeInstanceOf<Success<Unit>>()
    }

    @Test
    fun `can create KResult by catching an Exception`() {
        val e = KResult.catch {
            "test"
        }

        e.shouldBeInstanceOf<Success<String>>()
        e.value.shouldBeEqual("test")

        val f = KResult.catch {
            throw RuntimeException("ex")
        }

        f.shouldBeInstanceOf<Failure<Throwable>>()
        f.error.message!!.shouldBeEqual("ex")
    }
}