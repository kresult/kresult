// This file was automatically generated from README.md by Knit tool. Do not edit.
package io.kresult.examples.exampleReadme02

import io.kotest.matchers.shouldBe
import io.kresult.core.KResult
import io.kresult.core.filter
import io.kresult.core.flatMap
import io.kresult.core.flatten

fun test() {
    val res: KResult<Throwable, String> = KResult.Success("test")

    // map
    res
        .map { "$it-1" }
        .getOrNull() shouldBe "test-1"

    // flatMap
    res
        .flatMap {
            if (it.length > 3) {
                KResult.Success(it)
            } else {
                KResult.Failure(RuntimeException("missing length"))
            }
        }
        .getOrNull() shouldBe "test"

    // flatten
    val nestedRes: KResult<Throwable, KResult<Throwable, String>> =
        KResult.Success(res)

    nestedRes.flatten().getOrNull() shouldBe "test"

    // filter
    res
        .filter(
            { it.isNotBlank() },
            { RuntimeException("String is empty") }
        )
        .isSuccess() shouldBe true
}
