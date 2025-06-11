package com.dluong.domain.repository

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.domain.model.Pet
import kotlinx.coroutines.flow.Flow
import com.dluong.designsystem.core.domain.utils.Result

/**
 * Interface for managing favorite cats.
 *
 * This interface defines methods for voting up and down cats, as well as observing the list of favorite cats.
 */
interface FavoriteCatRepository {
    suspend fun voteDown(cat: Pet): Result<Unit, AppError>

    suspend fun voteUp(cat: Pet): Result<Unit, AppError>

    fun observeFavoriteCats(): Flow<List<Pet>>
}