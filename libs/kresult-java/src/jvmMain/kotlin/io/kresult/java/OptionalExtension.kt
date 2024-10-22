package io.kresult.java

import io.kresult.core.KResult
import java.util.*
import kotlin.jvm.optionals.getOrDefault


/**
 * <!--- TEST_NAME JavaIntegrationKnitTest -->
 */

/**
 * Transforms a Java [Optional] to a [KResult]
 *
 * ```kotlin
 * import io.kresult.java.toKResult
 * import io.kotest.matchers.shouldBe
 * import java.util.*
 *
 * fun test() {
 *   Optional.of("test")
 *     .toKResult()
 *     .isSuccess() shouldBe true
 * }
 * ```
 * <!--- KNIT example-java-01.kt -->
 * <!--- TEST lines.isEmpty() -->
 */
fun <T> Optional<T>.toKResult(): KResult<KResultJavaError, T> =
  this
    .map { KResult.Success(it) }
    .getOrDefault(KResult.Failure(KResultJavaError.NullError))

/**
 * Transforms a [KResult] to a Java [Optional]
 *
 * **Head up:** We're loosing type information of the [Failure] side when transforming a [KResult] to an [Optional], so
 * this operation is irreversible.
 *
 * ```kotlin
 * import io.kresult.core.KResult
 * import io.kresult.java.toOptional
 * import io.kotest.matchers.shouldBe
 *
 * fun test() {
 *   KResult.Success("test")
 *     .toOptional()
 *     .isPresent shouldBe true
 * }
 * ```
 * <!--- KNIT example-java-02.kt -->
 * <!--- TEST lines.isEmpty() -->
 */
fun <E, T : Any> KResult<E, T>.toOptional() =
  fold(
    { Optional.empty() },
    { Optional.of(it) }
  )

sealed class KResultJavaError(val message: String) {

  object NullError : KResultJavaError("Given element was null")
}
