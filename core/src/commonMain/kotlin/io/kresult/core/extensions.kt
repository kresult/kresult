package io.kresult.core

fun <T> Result<T>.asKResult(): KResult<Throwable, T> =
    fold(
        { t -> KSuccess(t) },
        { e -> KFailure(e) },
    )
