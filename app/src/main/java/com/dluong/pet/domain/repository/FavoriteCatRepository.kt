package com.dluong.pet.domain.repository

import com.dluong.pet.domain.model.Cat
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing favorite cats.
 *
 * This interface defines methods for voting up and down cats, as well as observing the list of favorite cats.
 */
interface FavoriteCatRepository {
    suspend fun voteDown(cat: Cat): Result<Unit>

    suspend fun voteUp(cat: Cat): Result<Unit>

    fun observeFavoriteCats(): Flow<Result<List<Cat>>>
}