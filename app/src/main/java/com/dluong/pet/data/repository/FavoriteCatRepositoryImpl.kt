package com.dluong.pet.data.repository

import com.dluong.pet.domain.model.Cat
import com.dluong.pet.domain.repository.FavoriteCatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [FavoriteCatRepository] that interacts with the database to manage favorite cats.
 *
 * @param databaseModule The database module used for data access.
 * @param ioDispatcher The dispatcher used for IO operations.
 *
 */
class FavoriteCatRepositoryImpl @Inject constructor() : FavoriteCatRepository {
    override suspend fun voteDown(cat: Cat): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun voteUp(cat: Cat): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun observeFavoriteCats(): Flow<Result<List<Cat>>> {
        TODO("Not yet implemented")
    }
}