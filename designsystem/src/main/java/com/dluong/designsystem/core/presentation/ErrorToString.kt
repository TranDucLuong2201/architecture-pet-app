package com.dluong.designsystem.core.presentation

import android.content.Context
import com.dluong.designsystem.R
import com.dluong.designsystem.core.domain.utils.AppError

fun AppError.Network.toString(context: Context): String {
    val resId = when (this) {
        AppError.Network.Timeout -> R.string.error_request_timeout
        AppError.Network.TooManyRequests -> R.string.error_too_many_requests
        AppError.Network.NoInternet -> R.string.error_no_internet
        AppError.Network.ServerError -> R.string.error_unknown
        AppError.Network.Serialization -> R.string.error_serialization
        AppError.Network.Forbidden -> R.string.error_unknown
        AppError.Network.NotFound -> R.string.error_unknown
        AppError.Network.Unauthorized -> R.string.error_unknown
        is AppError.Network.ApiError -> return context.getString(R.string.error_api, this.message)
        is AppError.Network.MitmAttack -> return context.getString(R.string.error_mitm)
    }
    return context.getString(resId)
}

fun AppError.Auth.toString(context: Context): String =
    when (this) {
        AppError.Auth.InvalidCredential -> context.getString(R.string.error_invalid_credential)
        AppError.Auth.TokenExpired -> context.getString(R.string.error_token_expired)
        AppError.Auth.Unauthenticated -> context.getString(R.string.error_unauthenticated)
        AppError.Auth.UserNotFound -> context.getString(R.string.error_user_not_found)
    }

fun AppError.Data.toString(context: Context): String =
    when (this) {
        AppError.Data.ConstraintViolation -> context.getString(R.string.error_constraint_violation)
        AppError.Data.Corrupt -> context.getString(R.string.error_data_corrupt)
        AppError.Data.NotFound -> context.getString(R.string.error_data_not_found)
        AppError.Data.ReadWriteError -> context.getString(R.string.error_read_write)
        AppError.Data.StorageFull -> context.getString(R.string.error_storage_full)
    }

fun AppError.User.toString(context: Context): String =
    when (this) {
        AppError.User.ActionNotAllowed -> context.getString(R.string.error_action_not_allowed)
        AppError.User.AlreadyLiked -> context.getString(R.string.error_already_liked)
        AppError.User.AlreadyReported -> context.getString(R.string.error_already_reported)
    }

fun AppError.Validation.toString(context: Context): String =
    when(this) {
        is AppError.Validation.Custom -> this.message
        AppError.Validation.EmptyField -> context.getString(R.string.error_empty_field)
        AppError.Validation.InvalidEmail -> context.getString(R.string.error_invalid_email)
        AppError.Validation.InvalidInput -> context.getString(R.string.error_invalid_input)
    }
fun Context.toastError(error: AppError): String {
    val message = when (error) {
        is AppError.Network -> error.toString(this)
        is AppError.Auth -> error.toString(this)
        is AppError.Data -> error.toString(this)
        is AppError.User -> error.toString(this)
        is AppError.Validation -> error.toString(this)
        is AppError.Unknown -> getString(R.string.error_unknown)
    }
    return message
}
