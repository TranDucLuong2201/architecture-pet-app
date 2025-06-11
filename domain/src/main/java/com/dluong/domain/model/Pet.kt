package com.dluong.domain.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val id: String,
    val urlImage: String,
    val width: Int,
    val height: Int,
    val isLiked: Boolean = false,
) : Parcelable {
    val isVideo: Boolean
        get() = urlImage.endsWith(".mp4", ignoreCase = true)
}
