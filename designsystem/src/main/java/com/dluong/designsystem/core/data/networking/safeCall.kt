package com.dluong.designsystem.core.data.networking

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * A function that safely executes a network call and returns a Result.
 *
 * @param T The type of the data returned on success.
 * @return A Result containing either the data or an error.
 */
@OptIn(ExperimentalContracts::class)
suspend inline fun <reified T> safeCall(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> Response<T>
): Result<T, AppError.Network> {
    contract { callsInPlace(block, InvocationKind.UNKNOWN) }

    return try {
        val response = withContext(context) { block() }
        when (response.code()) {
            in 200..299 -> {
                response.body()?.let { Result.Success(it) }
                    ?: Result.Error(AppError.Network.Serialization)
            }

            408 -> Result.Error(AppError.Network.Timeout)
            429 -> Result.Error(AppError.Network.TooManyRequests)
            in 500..599 -> Result.Error(AppError.Network.ServerError)
            else -> Result.Error(AppError.Network.Unauthorized)
        }
    } catch (c: CancellationException) {
        throw c
    } catch (e: java.nio.channels.UnresolvedAddressException) {
        Result.Error(AppError.Network.NoInternet)
    } catch (e: SerializationException) {
        Result.Error(AppError.Network.Serialization)
    } catch (e: IOException) {
        Result.Error(AppError.Network.NoInternet)
    } catch (e: Throwable) {
        coroutineContext.ensureActive()
        Result.Error(AppError.Network.Unauthorized)
    }
}

