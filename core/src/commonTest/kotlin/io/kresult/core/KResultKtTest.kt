package io.kresult.core

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class KResultKtTest {

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
    fun `can create KResult by catching an Exception`() {
        val e = KResult.catch {
            "test"
        }

        e.shouldBeInstanceOf<KSuccess<String>>()
        e.value.shouldBeEqual("test")

        val f = KResult.catch {
            throw RuntimeException("ex")
        }

        f.shouldBeInstanceOf<KFailure<Throwable>>()
        f.error.message!!.shouldBeEqual("ex")
    }
}