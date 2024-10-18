// This file was automatically generated from ArrowExtensions.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleArrow02

import io.kotest.matchers.shouldBe
import io.kresult.integration.arrow.toEither
import io.kresult.core.KResult

fun test() {
    KResult.Success("test")
        .toEither()
        .isRight() shouldBe true
}
