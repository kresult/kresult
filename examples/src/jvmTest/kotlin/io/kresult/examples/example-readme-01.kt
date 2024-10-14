// This file was automatically generated from README.md by Knit tool. Do not edit.
package io.kresult.examples.exampleReadme01

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult
import io.kresult.core.asKResult
import io.kresult.core.asSuccess

fun test() {
    // by instance
    KResult.Success("test")
        .isSuccess() shouldBe true

    // by extension function
    "test".asSuccess() shouldBe true

    // by catching exceptions
    KResult
        .catch {
            throw RuntimeException("throws")
        }
        .isFailure() shouldBe true

    // from nullable
    KResult
        .fromNullable(null) {
            RuntimeException("Value can not be null")
        }
        .isFailure() shouldBe true

    // from Kotlin Result
    Result.success("test")
        .asKResult()
        .isSuccess() shouldBe true

    // from Arrow Either (needs kresult-arrow extension)
    arrow.core.Either.Left("test")
}
