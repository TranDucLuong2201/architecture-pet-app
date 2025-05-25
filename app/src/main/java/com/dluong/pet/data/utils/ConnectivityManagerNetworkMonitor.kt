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
import timber.log.Timber
import javax.inject.Inject

class ConnectivityManagerNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {
    /**
     * A [NetworkCallback] instance to monitor network changes.
     * This is used to register and unregister the network callback.
     */
    private var networkCallback: NetworkCallback? = null


    private val networkRequest: NetworkRequest by lazy {
        NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
    }

    // The network callback instance to track network changes.
    override val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            Timber.e("ConnectivityManager is null, emitting false and completing.")
            channel.trySend(false)
            channel.close()
            return@callbackFlow
        }

        var activeNetworkCount = 0

        /**
         * The callback's methods are invoked on changes to *any* network matching the [NetworkRequest],
         * not just the active network. So we can simply track the presence (or absence) of such [Network].
         */
        val callback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                activeNetworkCount++
                channel.trySend(activeNetworkCount > 0)
                Timber.e("Network available: $network")
                super.onAvailable(network)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val connected = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(connected)
            }

            override fun onLost(network: Network) {
                activeNetworkCount--
                channel.trySend(activeNetworkCount > 0)
                Timber.e("Network lost: $network")
                super.onLost(network)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        channel.trySend(connectivityManager.isCurrentlyConnected())

        Timber.d("Network callback registered, initial connectivity: ${connectivityManager.isCurrentlyConnected()}")
        awaitClose {
            try {
                connectivityManager.unregisterNetworkCallback(callback)
                Timber.d("Network callback unregistered")
            } catch (e: Exception) {
                Timber.e("Error unregistering network callback: ${e.message}")
            }
        }
    }

    override suspend fun registerNetworkCallback() {
        context.getSystemService<ConnectivityManager>()?.let { connectivityManager ->
            try {
                networkCallback = object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        Timber.d("Network available: $network")
                    }

                    override fun onLost(network: Network) {
                        Timber.d("Network lost: $network")
                    }
                }
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
                Timber.d("Network callback registered")
            } catch (e: Exception) {
                Timber.e("Error registering network callback: ${e.message}")
            }
        } ?: Timber.e("ConnectivityManager is null, cannot register network callback")
    }

    override suspend fun unregisterNetworkCallback() {
        context.getSystemService<ConnectivityManager>()?.let { connectivityManager ->
            try {
                networkCallback?.let {
                    connectivityManager.unregisterNetworkCallback(it)
                    Timber.e("Network callback unregistered")
                }
            } catch (e: Exception) {
                Timber.e("Error unregistering network callback: ${e.message}")
            }
        }
    }
}

private fun ConnectivityManager.isCurrentlyConnected() =
    activeNetwork?.let(::getNetworkCapabilities)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true