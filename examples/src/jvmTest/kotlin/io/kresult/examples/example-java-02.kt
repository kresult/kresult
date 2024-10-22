// This file was automatically generated from OptionalExtension.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleJava02

import io.kresult.core.KResult
import io.kresult.java.toOptional
import io.kotest.matchers.shouldBe

fun test() {
  KResult.Success("test")
    .toOptional()
    .isPresent shouldBe true
}
