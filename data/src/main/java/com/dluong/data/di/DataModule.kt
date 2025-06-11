package com.dluong.data.di

import com.dluong.data.repository.FavoriteCatRepositoryImpl
import com.dluong.data.repository.VotePetRepositoryImp
import com.dluong.data.utils.ConnectivityManagerNetworkMonitor
import com.dluong.data.utils.NetworkMonitor
import com.dluong.domain.repository.FavoriteCatRepository
import com.dluong.domain.repository.VotePetRepository
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

    @Binds
    fun votePetRepositoryImpl(repository: VotePetRepositoryImp): VotePetRepository

    /**
     * Binds the implementation of [VotePetRepository] to its interface.
     * This allows for dependency injection of the repository implementation.
     *
     * @return The bound [VotePetRepository] instance.
     */


    /**
     * Binds the implementation of [NetworkMonitor] to its interface.
     * This allows for dependency injection of the network monitor implementation.
     *
     * @param networkMonitor The implementation of [NetworkMonitor].
     * @return The bound [NetworkMonitor] instance.
     */
    @Binds
    fun bindNetworkMonitor(networkMonitor: ConnectivityManagerNetworkMonitor): NetworkMonitor
}