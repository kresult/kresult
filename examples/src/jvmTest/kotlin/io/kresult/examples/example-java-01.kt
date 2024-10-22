// This file was automatically generated from OptionalExtension.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleJava01

import io.kresult.java.toKResult
import io.kotest.matchers.shouldBe
import java.util.*

fun test() {
  Optional.of("test")
    .toKResult()
    .isSuccess() shouldBe true
}
