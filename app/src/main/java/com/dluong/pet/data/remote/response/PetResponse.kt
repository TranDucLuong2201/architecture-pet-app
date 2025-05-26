package com.dluong.pet.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PetDto(
    @Json(name = "id") val id: String,
    @Json(name = "url") val urlImage: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
)

