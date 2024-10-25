package io.kresult.java

import io.kresult.core.KResult
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlin.test.Test

class OptionalExtensionTest {

  @Test
  fun `empty optional should result in failure`() {
    Optional.empty<Unit>().toKResult() shouldBeEqual KResult.Failure(KResultJavaError.NullError)
  }

  @Test
  fun `optional of string should result in success`() {
    Optional.of("test").toKResult() shouldBeEqual KResult.Success("test")
  }
}
