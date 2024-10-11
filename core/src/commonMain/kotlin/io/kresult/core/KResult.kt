package io.kresult.core

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

@OptIn(ExperimentalContracts::class)
sealed class KResult<out E, out T> {

    fun isFailure(): Boolean {
        contract {
            returns(true) implies (this@KResult is KFailure<E>)
            returns(false) implies (this@KResult is KSuccess<T>)
        }
        return this@KResult is KFailure<E>
    }

    fun isSuccess(): Boolean {
        contract {
            returns(true) implies (this@KResult is KSuccess<T>)
            returns(false) implies (this@KResult is KFailure<E>)
        }
        return this@KResult is KSuccess<T>
    }

    inline fun <C> fold(ifFailure: (failure: E) -> C, ifSuccess: (success: T) -> C): C {
        contract {
            callsInPlace(ifFailure, InvocationKind.AT_MOST_ONCE)
            callsInPlace(ifSuccess, InvocationKind.AT_MOST_ONCE)
        }
        return when (this) {
            is KSuccess -> ifSuccess(value)
            is KFailure -> ifFailure(error)
        }
    }

    inline fun <C> map(f: (success: T) -> C): KResult<E, C> {
        contract {
            callsInPlace(f, InvocationKind.AT_MOST_ONCE)
        }
        return flatMap { KSuccess(f(it)) }
    }

    inline fun <C> mapFailure(f: (E) -> C): KResult<C, T> {
        contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
        return when (this) {
            is KFailure -> KFailure(f(error))
            is KSuccess -> KSuccess(value)
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

    fun successOrNull(): T? {
        contract {
            returns(null) implies (this@KResult is KFailure<E>)
            returnsNotNull() implies (this@KResult is KSuccess<T>)
        }
        return getOrElse { null }
    }

    fun failureOrNull(): E? {
        contract {
            returnsNotNull() implies (this@KResult is KFailure<E>)
            returns(null) implies (this@KResult is KSuccess<T>)
        }
        return fold(
            { it },
            { null }
        )
    }

    fun swap(): KResult<T, E> =
        fold({ KSuccess(it) }, { KFailure(it) })

    companion object {
        @JvmStatic
        inline fun <R> catch(f: () -> R): KResult<Throwable, R> =
            kotlin.runCatching { f() }.asKResult()

    }
}

data class KFailure<out E>(val error: E) : KResult<E, Nothing>() {
    override fun toString(): String = "KFailure($error)"
}

data class KSuccess<out T>(val value: T) : KResult<Nothing, T>() {
    override fun toString(): String = "KSuccess($value)"

    companion object {
        @PublishedApi
        internal val unit: KResult<Nothing, Unit> = KSuccess(Unit)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <E, T, T1> KResult<E, T>.flatMap(f: (success: T) -> KResult<E, T1>): KResult<E, T1> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
        is KSuccess -> f(this.value)
        is KFailure -> this
    }
}

@OptIn(ExperimentalContracts::class)
fun <E, T, E1> KResult<E, T>.flatMapFailure(f: (failure: E) -> KResult<E1, T>): KResult<E1, T> {
    contract { callsInPlace(f, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
        is KFailure -> f(this.error)
        is KSuccess -> this
    }
}

fun <E, T> KResult<E, KResult<E, T>>.flatten(): KResult<E, T> =
    flatMap { it }

fun <E, T> KResult<KResult<E, T>, T>.flattenFailure(): KResult<E, T> =
    flatMapFailure { it }

@OptIn(ExperimentalContracts::class)
inline infix fun <E, T> KResult<E, T>.getOrElse(default: (E) -> T): T {
    contract { callsInPlace(default, InvocationKind.AT_MOST_ONCE) }
    return when (this) {
        is KFailure -> default(this.error)
        is KSuccess -> this.value
    }
}

fun <T> KResult<T, T>.merge(): T =
    fold(
        { it },
        { it }
    )

fun <A> A.asFailure(): KResult<A, Nothing> =
    KFailure(this)

fun <A> A.asSuccess(): KResult<Nothing, A> =
    KSuccess(this)

operator fun <E : Comparable<E>, T : Comparable<T>> KResult<E, T>.compareTo(other: KResult<E, T>): Int =
    fold(
        { a1 -> other.fold({ a2 -> a1.compareTo(a2) }, { -1 }) },
        { b1 -> other.fold({ 1 }, { b2 -> b1.compareTo(b2) }) }
    )

fun <E, T> KResult<E, T>.combine(
    other: KResult<E, T>,
    combineFailure: (E, E) -> E,
    combineSuccess: (T, T) -> T
): KResult<E, T> =
    when (val one = this) {
        is KFailure -> when (other) {
            is KFailure -> KFailure(combineFailure(one.error, other.error))
            is KSuccess -> one
        }

        is KSuccess -> when (other) {
            is KFailure -> other
            is KSuccess -> KSuccess(combineSuccess(one.value, other.value))
        }
    }