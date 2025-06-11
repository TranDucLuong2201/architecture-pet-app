package com.dluong.pet.utils

import com.dluong.data.api.ApiKeyProvider
import com.dluong.pet.BuildConfig
import javax.inject.Inject

class DefaultApiKeyProvider @Inject constructor() : ApiKeyProvider {
    override fun getApiKey(): String = BuildConfig.API_KEY
}