//package com.dluong.pet.presentation.ui
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.dluong.core.domain.utils.NetworkError
//import com.dluong.pet.PetsApplication
//import com.dluong.pet.data.utils.NetworkMonitor
//import com.dluong.pet.di.NetworkMonitorEntryPoint
//import dagger.hilt.android.EntryPointAccessors
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.receiveAsFlow
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import javax.inject.Inject
//
//@HiltViewModel
//open class NetworkStatusViewModel @Inject constructor(
//) : ViewModel() {
//    private val networkMonitor: NetworkMonitor by lazy {
//        EntryPointAccessors.fromApplication(
//            PetsApplication.instance,
//            NetworkMonitorEntryPoint::class.java
//        ).networkMonitor()
//    }
//    private val _networkError = MutableStateFlow<String?>(null)
//    val networkError: StateFlow<String?> = _networkError.asStateFlow()
//    private val _isOnline = MutableStateFlow(true) // Default to true to avoid blocking UI
//    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()
//
//
//    // This class can be extended by other ViewModels to provide common functionality
//    // or properties that are shared across multiple ViewModels in the application.
//    private val _events = Channel<PetsEvent>()
//    val events = _events.receiveAsFlow()
//
//    init {
//        observeNetwork()
//    }
//
//    private fun observeNetwork() {
//        viewModelScope.launch {
//            try {
//                networkMonitor.isOnline
//                    .catch { exception ->
//                        Timber.e(exception, "Error monitoring network connectivity")
//                        _networkError.value = "Unable to monitor network status"
//                        // Continue with assumed online state
//                        _isOnline.value = true
//                    }
//                    .collect { isConnected ->
//                        _isOnline.value = isConnected
//                        _networkError.value = null // Clear any previous errors
//                        Timber.d("Network connectivity changed: $isConnected")
//                    }
//            } catch (e: Exception) {
//                Timber.e(e, "Failed to start network monitoring")
//                _networkError.value = "Network monitoring unavailable"
//                _isOnline.value = true // Assume online to not block functionality
//            }
//        }
//    }
//
//    fun clearNetworkError() {
//        _networkError.value = null
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        try {
//            // Clean up if needed
//            Timber.d("NetworkStatusViewModel cleared")
//        } catch (e: Exception) {
//            Timber.e(e, "Error during NetworkStatusViewModel cleanup")
//        }
//    }
//}
//
///**
// * PetsEvent is a sealed interface that represents events related to pets.
// * It can be used to communicate success or error states in the application.
// */
//sealed interface PetsEvent {
//    data class Error(val error: NetworkError) : PetsEvent
//    data class Success(val message: String) : PetsEvent
//}