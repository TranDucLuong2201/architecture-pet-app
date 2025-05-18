package com.dluong.pet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dluong.pet.data.local.entity.FavoriteCatEntity
import io.reactivex.rxjava3.core.Observable

@Dao
interface FavoriteCatDao {
    @Query("SELECT * FROM favorite_cats ORDER BY created_at DESC")
    fun observerAll(): Observable<List<FavoriteCatEntity>>

    @Insert
    suspend fun insertMany(cats: List<FavoriteCatEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cat: FavoriteCatEntity): Long

    @Update
    suspend fun update(cat: FavoriteCatEntity)

    @Delete
    suspend fun delete(cat: FavoriteCatEntity)
}