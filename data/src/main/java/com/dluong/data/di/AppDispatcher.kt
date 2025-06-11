package com.dluong.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * AppDispatcher is a custom annotation used to qualify different CoroutineDispatchers.
 * It allows for the injection of specific dispatchers in the application.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppDispatcher(val petDispatcher: DispatcherType)

/**
 * DispatcherType is an enum class that defines different types of dispatchers.
 * It includes Default and IO dispatchers.
 */
enum class DispatcherType {
    Default,
    IO,
}

/**
 * DispatcherModule is a Dagger module that provides CoroutineDispatchers for the application.
 * It includes the IO and Default dispatchers.
 */
@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @Provides
    @AppDispatcher(DispatcherType.IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @AppDispatcher(DispatcherType.Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}