package com.dluong.pet.domain.repository

import com.dluong.core.domain.utils.NetworkError
import com.dluong.pet.domain.model.Pet
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing favorite cats.
 *
 * This interface defines methods for voting up and down cats, as well as observing the list of favorite cats.
 */
interface FavoriteCatRepository {
    suspend fun voteDown(cat: Pet): com.dluong.core.domain.utils.Result<Unit, NetworkError>

    suspend fun voteUp(cat: Pet): com.dluong.core.domain.utils.Result<Unit, NetworkError>

    fun observeFavoriteCats(): Flow<List<Pet>>
}