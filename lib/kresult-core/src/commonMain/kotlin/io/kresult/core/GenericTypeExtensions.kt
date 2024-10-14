package io.kresult.core

import io.kresult.core.KResult.Failure
import io.kresult.core.KResult.Success

// create from generic types outside the library

fun <T> Result<T>.asKResult(): KResult<Throwable, T> =
    fold(
        { t -> Success(t) },
        { e -> Failure(e) },
    )

fun <A> A.asFailure(): KResult<A, Nothing> =
    Failure(this)

fun <A> A.asSuccess(): KResult<Nothing, A> =
    Success(this)