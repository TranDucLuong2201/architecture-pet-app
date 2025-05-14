package com.dluong.pet.data.mapper

import com.dluong.pet.data.remote.response.CatResponse
import com.dluong.pet.domain.model.Cat

/**
 * Extension function to map [CatResponse] to [Cat] domain model.
 *
 * @return [Cat] domain model.
 */
fun CatResponse.toCatDomain() = Cat(
    id = id,
    url = urlImage,
    width = width,
    height = height,
)