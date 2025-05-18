package com.dluong.pet.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConnectivityManagerNetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
) : NetworkMonitor {
    private var networkCallback: NetworkCallback? = null

    // Observable that emits the current connectivity status.
    // It uses the ConnectivityManager to check if the device is connected to the internet.
    // If the ConnectivityManager is null, it emits false and completes.
    override val isConnected: Observable<Boolean> = Observable.create<Boolean> { emitter ->
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            emitter.onNext(false)
            emitter.onComplete()
            return@create
        }

        /**
         * The callback's methods are invoked on changes to *any* network matching the [NetworkRequest],
         * not just the active network. So we can simply track the presence (or absence) of such [Network].
         */
        val callback = object : NetworkCallback() {
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                emitter.onNext(networks.isNotEmpty())
                super.onAvailable(network)
            }

            override fun onLost(network: Network) {
                networks -= network
                emitter.onNext(networks.isNotEmpty())
                super.onLost(network)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        /**
         * Sends the latest connectivity status to the underlying channel.
         */
        try {
            connectivityManager.registerNetworkCallback(request, callback)

        } catch (e: Exception) {
            emitter.onError(e)
        }

        /**
         * Emit the current connectivity status when the observer subscribes.
         */
        emitter.onNext(connectivityManager.isCurrentlyConnected())

        /**
         * Unregister the network callback when the observer unsubscribes.
         */
        emitter.setCancellable {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
        // Conflate the emissions to avoid sending multiple updates for the same connectivity status.
        .distinctUntilChanged()
        .debounce(300, TimeUnit.MILLISECONDS)

    override suspend fun registerNetworkCallback() {
        val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Timber.d("Network available: $network")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Timber.d("Network lost: $network")
            }
        }
        try {
            connectivityManager.registerNetworkCallback(request, networkCallback!!)
            Timber.d("Network callback registered")

        } catch (e: Exception) {
            // Handle the exception
            Timber.e("Error registering network callback: ${e.message}")
        }
    }

    override suspend fun unregisterNetworkCallback() {
        val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return
        try {
            networkCallback?.let {
                connectivityManager.unregisterNetworkCallback(it)
                Timber.d("Network callback unregistered")
            }
        } catch (e: Exception) {
            Timber.e("Error unregistering network callback: ${e.message}")
        }
    }
}

private fun ConnectivityManager.isCurrentlyConnected() =
    activeNetwork?.let(::getNetworkCapabilities)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true