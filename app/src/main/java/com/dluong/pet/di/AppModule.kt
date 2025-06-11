package com.dluong.pet.di

import coil.ImageLoader
import com.dluong.data.di.ApiKeyPetsQualifier
import com.dluong.data.di.BaseUrlPetsQualifier
import com.dluong.pet.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Provides
    @ApiKeyPetsQualifier
    fun provideApiKey(): String = BuildConfig.API_KEY

    @Provides
    @BaseUrlPetsQualifier
    fun provideBaseUrl(): String = BuildConfig.API_DOMAIN

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: android.content.Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}