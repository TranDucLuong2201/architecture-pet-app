package com.dluong.pet.domain.repository

import com.dluong.core.domain.utils.NetworkError
import com.dluong.pet.domain.model.Pet
import kotlinx.coroutines.flow.Flow
import com.dluong.core.domain.utils.Result as NetworkResult

/**
 * Interface for managing favorite cats.
 *
 * This interface defines methods for voting up and down cats, as well as observing the list of favorite cats.
 */
interface FavoriteCatRepository {
    suspend fun voteDown(cat: Pet): Result<Unit>

    suspend fun voteUp(cat: Pet): Result<Unit>

    fun observeFavoriteCats(): Flow<NetworkResult<List<Pet>, NetworkError>>
}