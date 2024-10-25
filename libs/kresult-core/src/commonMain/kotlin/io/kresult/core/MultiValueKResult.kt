package io.kresult.core

import io.kresult.core.KResult.Success

/**
 * Typealias to represent [KResult] types that have a [List] type on the [Success] side
 *
 * @since 0.2.0
 * @see MultiErrorKResult if failure side carries a list
 */
typealias MultiValueKResult<E, T> = KResult<E, List<T>>
