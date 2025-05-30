package com.dluong.pet.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManagerNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {

    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            Timber.e("ConnectivityManager is null, emitting false and completing.")
            trySend(false)
            close()
            return@callbackFlow
        }

        // Send initial state
        trySend(connectivityManager.isCurrentlyConnected())

        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Timber.d("Network available: $network")
                trySend(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Timber.d("Network lost: $network")
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val connected = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                ) && networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(connected)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Timber.d("Network unavailable")
                trySend(false)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, callback)
            Timber.d("Network callback registered successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to register network callback")
            // Still try to provide connectivity info even if callback registration fails
            trySend(connectivityManager.isCurrentlyConnected())
        }

        awaitClose {
            try {
                connectivityManager.unregisterNetworkCallback(callback)
                Timber.d("Network callback unregistered")
            } catch (e: Exception) {
                Timber.e(e, "Error unregistering network callback")
            }
        }
    }.distinctUntilChanged() // Only emit when connectivity actually changes

    // Remove these methods as they're redundant and cause the TooManyRequestsException
    override suspend fun registerNetworkCallback() {
        // This is now handled in the Flow above
        Timber.d("Network callback registration handled by Flow")
    }

    override suspend fun unregisterNetworkCallback() {
        // This is now handled in the Flow's awaitClose
        Timber.d("Network callback unregistration handled by Flow")
    }
}

private fun ConnectivityManager.isCurrentlyConnected(): Boolean {
    return try {
        activeNetwork?.let(::getNetworkCapabilities)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } catch (e: Exception) {
        Timber.e(e, "Error checking network connectivity")
        false
    }
}