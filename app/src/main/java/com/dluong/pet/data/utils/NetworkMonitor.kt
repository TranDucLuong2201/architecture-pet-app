package com.dluong.pet.data.utils

import io.reactivex.rxjava3.core.Observable

interface NetworkMonitor {
    val isConnected: Observable<Boolean>

    suspend fun registerNetworkCallback()

    suspend fun unregisterNetworkCallback()
}