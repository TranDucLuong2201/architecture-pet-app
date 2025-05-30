
// FavoriteCatEntity.kt
package com.dluong.pet.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dluong.pet.domain.model.Pet
import java.time.Instant

@Entity(tableName = "favorite_cats")
data class FavoriteCatEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "width")
    val width: Int,
    @ColumnInfo(name = "height")
    val height: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
)

/**
 * Extension function to convert FavoriteCatEntity to Pet domain model.
 * Sets isLiked to true since this is a favorite pet.
 */
fun FavoriteCatEntity.toCatDomain(): Pet =
    Pet(
        id = id,
        urlImage = url,
        width = width,
        height = height,
        isLiked = true, // Always true for favorite pets
    )