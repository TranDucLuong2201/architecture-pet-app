package com.dluong.pet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.dluong.core.data.local.safeCallDb
import com.dluong.core.domain.utils.NetworkError
import com.dluong.pet.data.local.dao.FavoriteCatDao
import com.dluong.pet.data.local.entity.FavoriteCatEntity
import com.dluong.pet.data.local.entity.toCatDomain
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
import com.dluong.pet.utils.AppDispatcher
import com.dluong.pet.utils.DispatcherType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import com.dluong.core.domain.utils.Result as NetworkResult

/**
 * Implementation of [FavoriteCatRepository] that interacts with the database to manage favorite cats.
 *
 * @param ioDispatcher The dispatcher used for IO operations.
 *
 */
@RequiresApi(Build.VERSION_CODES.O)

class FavoriteCatRepositoryImpl @Inject constructor(
    private val favoriteCatDao: FavoriteCatDao,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : FavoriteCatRepository {
    override suspend fun voteDown(cat: Pet): Result<Unit> =
        safeCallDb(ioDispatcher) { favoriteCatDao.delete(cat.toFavoriteCatEntity()) }


    override suspend fun voteUp(cat: Pet): Result<Unit> =
        safeCallDb(ioDispatcher) { favoriteCatDao.insert(cat.toFavoriteCatEntity()) }


    override fun observeFavoriteCats(): Flow<NetworkResult<List<Pet>, NetworkError>> =
        favoriteCatDao
            .observerAll()
            .flowOn(ioDispatcher)
            .map { cats ->
                NetworkResult.Success(cats.map { it.toCatDomain() })
            }

}

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