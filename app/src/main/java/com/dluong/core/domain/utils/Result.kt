package com.dluong.core.domain.utils

/**
 * A sealed class representing the result of an operation.
 *
 */
typealias DomainError = Error

// A type alias for the Error class to represent domain-specific errors.
sealed interface Result<out D, out E : Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>
}


/**
 * Extension function to map the data of a Result.
 *
 * @param T The type of the data in the Result.
 * @param E The type of the error in the Result.
 * @param R The type of the new data after mapping.
 * @param map The mapping function to apply to the data.
 * @return A new Result with the mapped data or the same error.
 */
inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> Result.Error(error)
    }
}

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyDataResult<E> = map {}


inline fun <T, E : Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}
inline fun <T, E : Error, R> Result<T, E>.fold(
    onSuccess: (T) -> R,
    onFailure: (E) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onFailure(error)
}

/**
 * A type alias for a Result with no data.
 *
 * @param E The type of the error returned on failure.
 */
typealias EmptyDataResult<E> = Result<Unit, E>
