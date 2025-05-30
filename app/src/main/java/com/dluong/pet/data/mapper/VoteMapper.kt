package com.dluong.pet.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.dluong.pet.data.local.entity.FavoriteCatEntity
import com.dluong.pet.data.remote.dto.PetDto
import com.dluong.pet.domain.model.Pet
import java.time.Instant

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
 * Extension function to convert a Pet domain model to FavoriteCatEntity.
 * Uses current timestamp when adding to favorites.
 */


@RequiresApi(Build.VERSION_CODES.O)
internal fun Pet.toFavoriteCatEntity(): FavoriteCatEntity {
    return FavoriteCatEntity(
        id = id,
        url = urlImage,
        width = width,
        height = height,
        createdAt = Instant.now(),
    )
}
