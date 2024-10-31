// This file was automatically generated from KResult.kt by Knit tool. Do not edit.
package io.kresult.examples.exampleResult11

import io.kresult.core.KResult

fun test() {
  val first: KResult<String, String> = KResult.Success("test 1")
  val second: KResult<String, String> = KResult.Success("test 2")

  KResult.combine(
    first,
    second,
    { f1, f2 -> "$f1, $f2" },
    { s1, s2 -> "$s1, $s2" }
  )
}
