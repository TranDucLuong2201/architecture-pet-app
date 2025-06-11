package com.dluong.data.local

import android.content.Context
import android.os.Build
import androidx.annotation.AnyThread
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.dluong.data.local.dao.FavoriteCatDao
import com.dluong.data.local.entity.FavoriteCatEntity
import java.time.Instant
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Database(
    entities = [FavoriteCatEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    InstantTypeConverter::class,
)
abstract class PetsAppDatabase : RoomDatabase() {
    abstract fun favoriteCatDao(): FavoriteCatDao

    companion object {
        // Database name
        private var DATABASE_NAME = "pets_app.db"

        /**
         * Double-checked locking singleton pattern
         *
         * This pattern is used to ensure that the database instance is created only once
         * and is thread-safe.
         * @field INSTANCE The singleton instance of the database.
         */
        @Volatile
        private var INSTANCE: PetsAppDatabase? = null

        @AnyThread
        @JvmStatic
        fun getInstance(
            context: Context,
            queryExecutor: Executor
        ): PetsAppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PetsAppDatabase::class.java,
                    DATABASE_NAME
                )
                    // Use a separate thread for Room transactions to avoid deadlocks. This means that tests that run Room
                    // transactions can't use testCoroutines.scope.runBlockingTest, and have to simply use runBlocking instead.
                    .setTransactionExecutor(Executors.newSingleThreadExecutor())
                    // Run queries on background I/O thread.
                    .setQueryExecutor(queryExecutor)
                    .build()
                    // Build the database instance and assign it to INSTANCE
                    .also { INSTANCE = it }
            }
    }
}
/**
 * Type converter for [Instant] to be used with Room database.
 *
 * @see [Room documentation](https://developer.android.com/training/data-storage/room/migrating-db-versions)
 */
@Suppress("unused")
@RequiresApi(Build.VERSION_CODES.O)
class InstantTypeConverter {
    /**
     * Converts an [Instant] to a [Long] representing the epoch milliseconds.
     *
     * @param value The [Instant] to convert.
     * @return The epoch milliseconds as a [Long], or null if the input is null.
     */
    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilli()
    }

    /**
     * Converts a [Long] representing the epoch milliseconds to an [Instant].
     *
     * @param value The epoch milliseconds as a [Long].
     * @return The corresponding [Instant], or null if the input is null.
     */
    @TypeConverter
    fun toInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }
}