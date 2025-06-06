package com.dluong.core.presentation

import android.content.Context
import com.dluong.core.domain.utils.NetworkError
import com.dluong.pet.R

fun NetworkError.toString(context: Context): String {
    val resId = when (this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_unknown
        NetworkError.SERIALIZATION -> R.string.error_serialization
        NetworkError.UNKNOWN -> R.string.error_unknown
        NetworkError.CONSTRAINT_VIOLATION -> TODO()
        is NetworkError.Custom -> TODO()
        NetworkError.DATABASE_ERROR -> TODO()
        NetworkError.DATABASE_FULL -> TODO()
        NetworkError.NO_INTERNET_CONNECTION -> TODO()
    }
    return context.getString(resId)
}