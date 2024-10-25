package io.kresult.it

import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.Success
import io.kresult.core.asSuccess
import io.kresult.core.flatMap
import kotlin.test.Test

class SimpleIntegrationTest {

  @Test
  fun `create map and get success`() {
    "test".asSuccess()
      .flatMap {
        if (it.length > 3) {
          Success(it)
        } else {
          Failure("String is not long enough")
        }
      }
      .getOrNull()
  }
}
