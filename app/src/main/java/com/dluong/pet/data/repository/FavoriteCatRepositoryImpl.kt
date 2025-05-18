package com.dluong.pet.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.dluong.pet.data.local.dao.FavoriteCatDao
import com.dluong.pet.data.local.entity.FavoriteCatEntity
import com.dluong.pet.data.local.entity.toCatDomain
import com.dluong.pet.domain.model.Cat
import com.dluong.pet.domain.repository.FavoriteCatRepository
import com.dluong.pet.utils.AppDispatcher
import com.dluong.pet.utils.DispatcherType
import com.dluong.pet.utils.runSuspendCatching
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import java.time.Instant
import javax.inject.Inject

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

    override suspend fun voteDown(cat: Cat) =
        runSuspendCatching(ioDispatcher) {
            favoriteCatDao.delete(cat.toFavoriteCatEntity())
        }

    override suspend fun voteUp(cat: Cat) =
        runSuspendCatching(ioDispatcher) {
            favoriteCatDao.insert(cat.toFavoriteCatEntity())
            Unit
        }

    override fun observeFavoriteCats() = favoriteCatDao
        .observerAll()
        .map { it.map(FavoriteCatEntity::toCatDomain) }
        .map { Result.success(it) }
        .onErrorReturn { Result.failure(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}

@RequiresApi(Build.VERSION_CODES.O)
internal fun Cat.toFavoriteCatEntity(): FavoriteCatEntity {
    return FavoriteCatEntity(
        id = id,
        url = url,
        width = width,
        height = height,
        createdAt = Instant.now(),
    )
}