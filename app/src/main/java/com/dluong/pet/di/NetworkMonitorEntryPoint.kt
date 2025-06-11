package com.dluong.pet.di

import com.dluong.data.utils.NetworkMonitor
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NetworkMonitorEntryPoint {
    fun networkMonitor(): NetworkMonitor
}