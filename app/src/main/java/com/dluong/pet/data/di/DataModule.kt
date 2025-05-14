package com.dluong.pet.data.di

import com.dluong.pet.data.repository.FavoriteCatRepositoryImpl
import com.dluong.pet.data.repository.VoteCatRepositoryImpl
import com.dluong.pet.domain.repository.FavoriteCatRepository
import com.dluong.pet.domain.repository.VoteCatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    /**
     * Binds the implementation of [FavoriteCatRepository] to its interface.
     * This allows for dependency injection of the repository implementation.
     *
     * @param repository The implementation of [FavoriteCatRepository].
     * @return The bound [FavoriteCatRepository] instance.
     */
    @Binds
    fun favoriteCatRepositoryImpl(repository: FavoriteCatRepositoryImpl): FavoriteCatRepository

    /**
     * Binds the implementation of [VoteCatRepository] to its interface.
     * This allows for dependency injection of the repository implementation.
     *
     * @param repository The implementation of [VoteCatRepository].
     * @return The bound [VoteCatRepository] instance.
     */
    @Binds
    fun bindVoteCatRepository(repository: VoteCatRepositoryImpl): VoteCatRepository
}