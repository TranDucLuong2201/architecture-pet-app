package com.dluong.pet.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dluong.core.domain.utils.NetworkError
import com.dluong.pet.PetsApplication
import com.dluong.pet.data.utils.NetworkMonitor
import com.dluong.pet.di.NetworkMonitorEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class NetworkStatusViewModel @Inject constructor(
) : ViewModel() {
    private val networkMonitor: NetworkMonitor by lazy {
        EntryPointAccessors.fromApplication(
            PetsApplication.instance,
            NetworkMonitorEntryPoint::class.java
        ).networkMonitor()
    }

    // This class can be extended by other ViewModels to provide common functionality
    // or properties that are shared across multiple ViewModels in the application.
    protected val _events = Channel<PetsEvent>()
    val events = _events.receiveAsFlow()

    private fun observeNetwork() {
        viewModelScope.launch {
            var lastStatus: Boolean? = null
            networkMonitor.isOnline.collect { connected ->
                if (connected != lastStatus) {
                    lastStatus = connected
                    if (connected) {
                        _events.send(PetsEvent.Success("Connected to internet"))
                    } else {
                        _events.send(PetsEvent.Error(NetworkError.NO_INTERNET))
                    }
                }
            }
        }
    }

    init {
        observeNetwork()
    }
}

/**
 * PetsEvent is a sealed interface that represents events related to pets.
 * It can be used to communicate success or error states in the application.
 */
sealed interface PetsEvent {
    data class Error(val error: NetworkError) : PetsEvent
    data class Success(val message: String) : PetsEvent
}