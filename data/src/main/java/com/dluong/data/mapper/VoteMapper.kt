package com.dluong.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.dluong.data.local.entity.FavoriteCatEntity
import com.dluong.data.remote.dto.PetDto
import com.dluong.domain.model.Pet
import java.time.Instant

/**
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
