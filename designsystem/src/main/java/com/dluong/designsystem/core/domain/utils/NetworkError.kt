package com.dluong.designsystem.core.domain.utils

/**
 * Represents network and database operation errors
 */

sealed class AppError : DomainError {

    // ========== Network related ==========
    sealed class Network : AppError() {
        data object NoInternet : Network()
        data object Timeout : Network()
        data object ServerError : Network()
        data object Unauthorized : Network()
        data object Forbidden : Network()
        data object NotFound : Network()
        data object TooManyRequests : Network()
        data object Serialization : Network()
        data class ApiError(val code: Int, val message: String) : Network()
        data class MitmAttack(val detail: String) : Network()
    }

    // ========== Auth related ==========
    sealed class Auth : AppError() {
        data object Unauthenticated : Auth()
        data object UserNotFound : Auth()
        data object TokenExpired : Auth()
        data object InvalidCredential : Auth()
    }

    // ========== Validation errors ==========
    sealed class Validation : AppError() {
        data object InvalidEmail : Validation()
        data object EmptyField : Validation()
        data object InvalidInput : Validation()
        data class Custom(val message: String) : Validation()
    }

    // ========== Data / Database / Cache ==========
    sealed class Data : AppError() {
        data object NotFound : Data()
        data object Corrupt : Data()
        data object ConstraintViolation : Data()
        data object StorageFull : Data()
        data object ReadWriteError : Data()
    }

    // ========== UI Logic / User action ==========
    sealed class User : AppError() {
        data object ActionNotAllowed : User()
        data object AlreadyLiked : User()
        data object AlreadyReported : User()
    }

    // ========== Fallback ==========
    data object Unknown : AppError()
}
