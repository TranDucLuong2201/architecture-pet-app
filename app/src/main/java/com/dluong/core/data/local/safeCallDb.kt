package com.dluong.core.data.local

import android.database.SQLException
import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


suspend inline fun <T> safeCallDb(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T
): Result<T, NetworkError> {
    return try {
        val result = withContext(context) { block() }
        Result.Success(result)
    } catch (e: Throwable) {
        // Convert exception to NetworkError
        val networkError = when (e) {
            is SQLException -> NetworkError.DATABASE_ERROR
            is IllegalStateException -> NetworkError.UNKNOWN
            else -> NetworkError.UNKNOWN
        }
        Result.Error(networkError)
    }
}