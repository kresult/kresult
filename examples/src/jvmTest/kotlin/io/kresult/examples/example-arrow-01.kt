// This file was automatically generated from ArrowExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleArrow01

import arrow.core.Either
import io.kotest.matchers.shouldBe
import io.kresult.arrow.toKResult

fun test() {
    Either.Right("test")
        .toKResult()
        .isSuccess() shouldBe true
}
