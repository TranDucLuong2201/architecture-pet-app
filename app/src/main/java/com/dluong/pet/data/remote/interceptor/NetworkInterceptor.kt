package com.dluong.pet.data.remote.interceptor

import com.dluong.pet.data.di.ApiKeyPetsQualifier
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Interceptor to add the API key to the request header.
 *
 * @param apiKey The API key to be added to the request header.
 */
class NetworkInterceptor
@Inject constructor(
    @ApiKeyPetsQualifier val apiKey: String,
) : Interceptor {
    companion object {
        private const val HEADER_API_KEY = "x-api-key"
    }

    /**
     * Adds the API key to the request header.
     */
    override fun intercept(chain: Interceptor.Chain): Response =
        chain
            .request()
            .newBuilder()
            .addHeader(HEADER_API_KEY, apiKey)
            .build()
            .let(chain::proceed)
}