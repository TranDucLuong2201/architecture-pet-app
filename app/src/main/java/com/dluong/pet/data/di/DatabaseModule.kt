package com.dluong.pet.data.di

import android.content.Context
import com.dluong.pet.data.local.PetsAppDatabase
import com.dluong.pet.data.local.dao.FavoriteCatDao
import com.dluong.pet.domain.repository.VotePetRepository
import com.dluong.pet.utils.AppDispatcher
import com.dluong.pet.utils.DispatcherType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

/**
 * Module that provides the database and DAO instances for the application.
 *
 * @see [PetsAppDatabase]
 * @see [FavoriteCatDao]
 */
@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {
    /**
     * Provides an instance of [PetsAppDatabase] for the application.
     *
     * @param appContext The application context.
     * @param ioDispatcher The IO dispatcher for database operations.
     * @return An instance of [PetsAppDatabase].
     */
    companion object {
        @Provides
        @Singleton
        fun providePetsAppDatabase(
            @ApplicationContext appContext: Context,
            @AppDispatcher(DispatcherType.IO) ioDispatcher: CoroutineDispatcher,
        ): PetsAppDatabase =
            PetsAppDatabase.getInstance(
                context = appContext,
                queryExecutor = ioDispatcher.asExecutor(),
            )

        @Provides
        fun provideFavoriteCatDao(petsAppDatabase: PetsAppDatabase): FavoriteCatDao =
            petsAppDatabase.favoriteCatDao()
    }

    /**
     * Provides an instance of [FavoriteCatDao] for the application.
     *
     * @param petsAppDatabase The database instance.
     * @return An instance of [FavoriteCatDao].
     */


    @Provides
    @Singleton
    fun provideIoDispatcher(
        @AppDispatcher(DispatcherType.IO) ioDispatcher: CoroutineDispatcher
    ): CoroutineDispatcher = ioDispatcher

}