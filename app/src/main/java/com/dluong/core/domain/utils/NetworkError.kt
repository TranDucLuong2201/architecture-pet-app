package com.dluong.core.domain.utils

/**
 * Represents network and database operation errors
 */
sealed class NetworkError : Error {

    // Network-related errors
    object NO_INTERNET : NetworkError()
    object REQUEST_TIMEOUT : NetworkError()
    object TOO_MANY_REQUESTS : NetworkError()
    object NO_INTERNET_CONNECTION : NetworkError()
    object SERVER_ERROR : NetworkError()
    object SERIALIZATION : NetworkError()
    object UNKNOWN : NetworkError()

    // Database-related errors
    object DATABASE_ERROR : NetworkError()
    object DATABASE_FULL : NetworkError()
    object CONSTRAINT_VIOLATION : NetworkError()

    // Custom error with message
    data class Custom(val message: String) : NetworkError()
}