package com.dluong.pet.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule is a Dagger module that provides application-wide dependencies.
 * It includes the Moshi JSON library for serialization and deserialization.
 * **/
@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    /**
     * Provides a Moshi instance for JSON serialization and deserialization.
     * @return A Moshi instance with KotlinJsonAdapterFactory added.
     */
    companion object {
        @Provides
        @Singleton
        fun provideMoshi(): Moshi =
            Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
    }
}