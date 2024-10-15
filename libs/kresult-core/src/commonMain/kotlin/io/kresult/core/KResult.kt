package io.kresult.core

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

/**
 * <!--- TEST_NAME KResultKnitTest -->
 *
 *
 */
@OptIn(ExperimentalContracts::class)
sealed class KResult<out E, out T> {

    /**
     * Indicates if a [KResult] is a [Failure]
     *
     * ```kotlin
     * import io.kresult.core.*
     * import io.kotest.matchers.shouldBe
     *
     * fun test() {
     *   KResult.Failure("test").isFailure() shouldBe true
     *   KResult.Success("test").isFailure() shouldBe false
     * }
     * ```
     * example-result-01.kt
     *
     * <!--- KNIT example-result-01.kt -->
     * <!-- TEST -->
     */
    fun isFailure(): Boolean {
        contract {
            returns(true) implies (this@KResult is Failure<E>)
            returns(false) implies (this@KResult is Success<T>)
        }
        return this@KResult is Failure<E>
    }

    /**
     * Indicated if [Result] is a [Success]
     */
    fun isSuccess(): Boolean {
        contract {
            returns(true) implies (this@KResult is Success<T>)
            returns(false) implies (this@KResult is Failure<E>)
        }
        return this@KResult is Success<T>
    }

    inline fun <C> fold(ifFailure: (failure: E) -> C, ifSuccess: (success: T) -> C): C {
        contract {
            callsInPlace(ifFailure, InvocationKind.AT_MOST_ONCE)
            callsInPlace(ifSuccess, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is Success -> ifSuccess(value)
            is Failure -> ifFailure(error)
        }
    }

    inline fun <C> map(f: (success: T) -> C): KResult<E, C> {
        contract {
            callsInPlace(f, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { Success(f(it)) }
    }

    inline fun <C> mapFailure(f: (E) -> C): KResult<C, T> {
        contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
        return when (this) {
            is Failure -> Failure(f(error))
            is Success -> Success(value)
        }
    }

    inline fun onSuccess(action: (success: T) -> Unit): KResult<E, T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        return also { if (it.isSuccess()) action(it.value) }
    }

    inline fun onFailure(action: (failure: E) -> Unit): KResult<E, T> {
        contract {
            callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        }
        return also { if (it.isFailure()) action(it.error) }
    }

    fun getOrNull(): T? {
        contract {
            returns(null) implies (this@KResult is Failure<E>)
            returnsNotNull() implies (this@KResult is Success<T>)
        }
        return getOrElse { null }
    }

    fun failureOrNull(): E? {
        contract {
            returnsNotNull() implies (this@KResult is Failure<E>)
            returns(null) implies (this@KResult is Success<T>)
        }
        return fold(
            { it },
            { null }
        )
    }

    /**
     * Swap the parameters ([T] and [E]) of this [Result].
     *
     * ```kotlin
     * import io.kresult.core.*
     * import io.kotest.matchers.shouldBe
     *
     * fun test() {
     *   KResult.Failure("test").swap() shouldBe KResult.Success("test")
     *   KResult.Success("test").swap() shouldBe KResult.Failure("test")
     * }
     * ```
     * <!--- KNIT example-result-02.kt -->
     * <!-- TEST -->
     */
    fun swap(): KResult<T, E> =
        fold({ Success(it) }, { Failure(it) })

    companion object {

        @JvmStatic
        inline fun <T> catch(f: () -> T): KResult<Throwable, T> =
            kotlin.runCatching { f() }
                .asKResult()

        fun <T, E> fromNullable(value: T?, errFn: () -> E): KResult<E, T> =
            value
                ?.asSuccess()
                ?: errFn().asFailure()

        fun <T> fromNullable(value: T?): KResult<Throwable, T> =
            fromNullable(value) {
                NullPointerException("Value null!")
            }

  }

    data class Failure<out E>(val error: E) : KResult<E, Nothing>() {
        override fun toString(): String = "${this::class.simpleName}($error)"
    }

    data class Success<out T>(val value: T) : KResult<Nothing, T>() {
        override fun toString(): String = "${this::class.simpleName}($value)"

        companion object {
            @PublishedApi
            internal val unit: KResult<Nothing, Unit> = Success(Unit)
        }
    }
}


