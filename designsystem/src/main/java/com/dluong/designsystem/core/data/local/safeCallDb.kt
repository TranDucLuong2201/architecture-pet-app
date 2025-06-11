package com.dluong.designsystem.core.data.local

import android.database.SQLException
import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.Result
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


suspend inline fun <T> safeCallDb(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T
): Result<T, AppError> {
    return try {
        val result = withContext(context) { block() }
        Result.Success(result)
    } catch (e: Throwable) {
        // Convert exception to NetworkError
        val networkError = when (e) {
            is SQLException -> AppError.Data.ReadWriteError
            is IllegalStateException -> AppError.Data.Corrupt
            else -> AppError.Data.NotFound
        }
        Result.Error(networkError)
    }
}