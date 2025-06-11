// FavoriteCatRepositoryImpl.kt
package com.dluong.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.dluong.data.di.AppDispatcher
import com.dluong.data.di.DispatcherType
import com.dluong.data.local.dao.FavoriteCatDao
import com.dluong.data.local.entity.FavoriteCatEntity
import com.dluong.data.local.entity.toCatDomain
import com.dluong.data.mapper.toFavoriteCatEntity
import com.dluong.designsystem.core.data.local.safeCallDb
import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.domain.model.Pet
import com.dluong.domain.repository.FavoriteCatRepository
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
    override suspend fun voteDown(cat: Pet): com.dluong.designsystem.core.domain.utils.Result<Unit,AppError> =
        safeCallDb(ioDispatcher) {
            favoriteCatDao.delete(cat.toFavoriteCatEntity())
        }

    /**
     * Add a cat to favorites (vote up).
     */
    override suspend fun voteUp(cat: Pet): com.dluong.designsystem.core.domain.utils.Result<Unit, AppError> =
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