package com.dluong.pet.data.di

import androidx.room.processor.Context
import coil3.ImageLoader
import coil3.request.crossfade
import com.dluong.core.data.networking.HttpClientFactory
import com.dluong.pet.BuildConfig
import com.dluong.pet.data.remote.PetService
import com.dluong.pet.data.remote.interceptor.NetworkInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * NetworkModule is a Dagger module that provides network-related dependencies.
 * It includes the OkHttpClient, Retrofit instance, and API service.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrlPetsQualifier

/**
 * Qualifier for the API key used in network requests.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKeyPetsQualifier

/**
 * Qualifier for the Retrofit instance used for API requests.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiRequest

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a logging interceptor for network requests.
     * @return An Interceptor that logs network requests and responses.
     */
    @Provides
    @BaseUrlPetsQualifier
    fun provideBaseUrlPets(): String = BuildConfig.API_DOMAIN

    /**
     * Provides an interceptor for adding the API key to requests.
     * @return An Interceptor that adds the API key to the request headers.
     */
    @Provides
    @ApiKeyPetsQualifier
    fun provideApiKeyClient(): String = BuildConfig.API_KEY

    /**
     * Provides an HTTP logging interceptor for debugging network requests.
     * @return An Interceptor that logs network requests and responses.
     */
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    /**
     * Provides an HTTP logging interceptor for debugging network requests.
     * @param authorizationInterceptor The interceptor for adding the API key to requests.
     * @param httpLoggingInterceptor The interceptor for logging network requests.
     * @return An Interceptor that logs network requests and responses.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authorizationInterceptor: NetworkInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addInterceptor(authorizationInterceptor)
            .build()

    /**
     * Provides a Retrofit instance for the PetService API.
     * @param moshi The Moshi instance for JSON serialization/deserialization.
     * @param okHttpClient The OkHttpClient instance for network requests.
     * @param baseUrl The base URL for the API.
     * @return A Retrofit instance configured for the PetService API.
     */
    @Provides
    @Singleton
    @ApiRequest
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
        @BaseUrlPetsQualifier baseUrl: String,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    /**
     * Provides the PetService API interface.
     * This interface defines the endpoints for the PetService API.
     * @param retrofit The Retrofit instance for making network requests.
     */
    @Provides
    fun providePetsService(
        @ApiRequest retrofit: Retrofit,
    ): PetService = retrofit.create(PetService::class.java)

    @Provides
    @Singleton
    fun provideHttpClientFactory(): HttpClient = HttpClientFactory.create(CIO.create())

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: android.content.Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

}