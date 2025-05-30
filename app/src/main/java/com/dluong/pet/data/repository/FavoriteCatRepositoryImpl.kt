// FavoriteCatRepositoryImpl.kt
package com.dluong.pet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.dluong.core.data.local.safeCallDb
import com.dluong.core.data.networking.safeCall
import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
import com.dluong.pet.data.local.dao.FavoriteCatDao
import com.dluong.pet.data.local.entity.FavoriteCatEntity
import com.dluong.pet.data.local.entity.toCatDomain
import com.dluong.pet.data.mapper.toFavoriteCatEntity
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
import com.dluong.pet.utils.AppDispatcher
import com.dluong.pet.utils.DispatcherType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [FavoriteCatRepository] that interacts with the database to manage favorite cats.
 *
 * @param favoriteCatDao DAO for accessing favorite cat entities.
 * @param ioDispatcher CoroutineDispatcher for IO operations.
 */
@RequiresApi(Build.VERSION_CODES.O)
class FavoriteCatRepositoryImpl @Inject constructor(
    private val favoriteCatDao: FavoriteCatDao,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : FavoriteCatRepository {

    /**
     * Remove a cat from favorites (vote down).
     */
    override suspend fun voteDown(cat: Pet): Result<Unit, NetworkError> =
        safeCallDb(ioDispatcher) {
            favoriteCatDao.delete(cat.toFavoriteCatEntity())
        }

    /**
     * Add a cat to favorites (vote up).
     */
    override suspend fun voteUp(cat: Pet): Result<Unit, NetworkError> =
        safeCallDb(ioDispatcher) {
            favoriteCatDao.insert(cat.toFavoriteCatEntity())
            // Return success even if the item already exists (OnConflictStrategy.IGNORE)
            Unit
        }

    /**
     * Observe the list of favorite cats as a Flow.
     * Maps database entities to domain model.
     */
    override fun observeFavoriteCats(): Flow<List<Pet>> =
        favoriteCatDao
            .observerAll()
            .map { entities ->
                entities.map(FavoriteCatEntity::toCatDomain)
            }
            .flowOn(ioDispatcher)
}