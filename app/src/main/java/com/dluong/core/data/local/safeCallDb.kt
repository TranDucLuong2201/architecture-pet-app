package com.dluong.core.data.local

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend inline fun <T> safeCallDb(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T
): Result<T> {
    return try {
        val result = withContext(context) { block() }
        Result.success(result)
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
