package com.dluong.pet.data.mapper

import com.dluong.pet.data.remote.response.PetDto
import com.dluong.pet.domain.model.Pet

/**
 * Extension function to map [PetResponse] to [Pet] domain model.
 *
 * @return [Pet] domain model.
 */
fun PetDto.toPet() = Pet(
    id = id,
    urlImage = urlImage,
    width = width,
    height = height,
)
/**
 * Extension function to map [PetResponse] to a list of [Pet] domain models.
 *
 * @return List of [Pet] domain models.
 */
