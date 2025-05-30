package com.dluong.core.data.networking

import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
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
): Result<T, NetworkError> {
    contract { callsInPlace(block, InvocationKind.UNKNOWN) }

    return try {
        val response = withContext(context) { block() }
        when (response.code()) {
            in 200..299 -> {
                response.body()?.let { Result.Success(it) } ?: Result.Error(NetworkError.SERIALIZATION)
            }
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    } catch (c: CancellationException) {
        throw c
    } catch (e: java.nio.channels.UnresolvedAddressException) {
        Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        Result.Error(NetworkError.SERIALIZATION)
    } catch (e: IOException) {
        Result.Error(NetworkError.NO_INTERNET)
    } catch (e: Throwable) {
        coroutineContext.ensureActive()
        Result.Error(NetworkError.UNKNOWN)
    }
}

