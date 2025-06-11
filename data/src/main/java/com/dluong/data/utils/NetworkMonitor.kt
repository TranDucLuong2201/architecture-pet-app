package com.dluong.data.utils

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val isOnline: Flow<Boolean>
    suspend fun registerNetworkCallback()
    suspend fun unregisterNetworkCallback()
}