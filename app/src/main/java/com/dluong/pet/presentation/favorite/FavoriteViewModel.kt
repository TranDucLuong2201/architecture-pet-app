package com.dluong.pet.presentation.favorite

import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigator
import com.dluong.core.domain.utils.onError
import com.dluong.core.domain.utils.onSuccess
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
import com.dluong.pet.domain.usecase.ObserveFavoriteCatsUseCase
import com.dluong.pet.domain.usecase.VoteDownPetUseCase
import com.dluong.pet.domain.usecase.VoteUpPetUseCase
import com.dluong.pet.presentation.ui.NetworkStatusViewModel
import com.dluong.pet.presentation.ui.ext.MainDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val observeFavoriteCatsUseCase: ObserveFavoriteCatsUseCase,
    private val voteDownPetUseCase: VoteDownPetUseCase,
    private val voteUpPetUseCase: VoteUpPetUseCase,
    private val petRepository: FavoriteCatRepository
) : NetworkStatusViewModel() {

    private val _favoriteState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val favoriteState: StateFlow<FavoriteUiState> = _favoriteState.asStateFlow()

    private val _singleEvent = Channel<FavoriteSingleEvent>(Channel.UNLIMITED)
    val singleEvent = _singleEvent

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        observeFavoritePets()
    }

    private fun observeFavoritePets() {
        viewModelScope.launch {
            observeFavoriteCatsUseCase()
                .catch { exception ->
                    Timber.e(exception, "Error observing favorite pets")
                    _favoriteState.value = FavoriteUiState.Error("Failed to load favorite pets")
                    _singleEvent.trySend(FavoriteSingleEvent.LoadError("Failed to load favorite pets"))
                }
                .collect { favoritePets ->
                    _favoriteState.value = if (favoritePets.isEmpty()) {
                        FavoriteUiState.Empty
                    } else {
                        FavoriteUiState.Success(favoritePets)
                    }
                    _isRefreshing.value = false
                }
        }
    }

    fun toggleLike(pet: Pet) {
        viewModelScope.launch {
            try {
                if (pet.isLiked) {
                    // Remove from favorites
                    voteDownPetUseCase(pet)
                        .onSuccess {
                            Timber.d("Successfully removed pet ${pet.id} from favorites")
                            _singleEvent.trySend(FavoriteSingleEvent.PetRemovedFromFavorites(pet.id))
                        }
                        .onError { error ->
                            Timber.e("Failed to remove pet from favorites: $error")
                            _singleEvent.trySend(FavoriteSingleEvent.ToggleLikeError("Failed to remove from favorites"))
                        }
                } else {
                    // Add to favorites (unlikely in favorites screen but for consistency)
                    voteUpPetUseCase(pet)
                        .onSuccess {
                            Timber.d("Successfully added pet ${pet.id} to favorites")
                            _singleEvent.trySend(FavoriteSingleEvent.PetAddedToFavorites(pet.id))
                        }
                        .onError { error ->
                            Timber.e("Failed to add pet to favorites: $error")
                            _singleEvent.trySend(FavoriteSingleEvent.ToggleLikeError("Failed to add to favorites"))
                        }
                }
            } catch (e: Exception) {
                Timber.e(e, "Exception while toggling like for pet ${pet.id}")
                _singleEvent.trySend(FavoriteSingleEvent.ToggleLikeError("Something went wrong"))
            }
        }
    }

    fun removeFromFavorites(pet: Pet) {
        viewModelScope.launch {
            try {
                voteDownPetUseCase(pet)
                    .onSuccess {
                        Timber.d("Successfully removed pet ${pet.id} from favorites")
                        _singleEvent.trySend(FavoriteSingleEvent.PetRemovedFromFavorites(pet.id))
                    }
                    .onError { error ->
                        Timber.e("Failed to remove pet from favorites: $error")
                        _singleEvent.trySend(FavoriteSingleEvent.ToggleLikeError("Failed to remove from favorites"))
                    }
            } catch (e: Exception) {
                Timber.e(e, "Exception while removing pet ${pet.id} from favorites")
                _singleEvent.trySend(FavoriteSingleEvent.ToggleLikeError("Something went wrong"))
            }
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        // Since we're using Flow, the data will automatically refresh
        // The isRefreshing flag will be reset when new data arrives
    }

    fun clearAllFavorites() {
        viewModelScope.launch {
            val currentState = _favoriteState.value
            if (currentState is FavoriteUiState.Success) {
                val pets = currentState.data
                try {
                    pets.forEach { pet ->
                        voteDownPetUseCase(pet)
                            .onError { error ->
                                Timber.e("Failed to remove pet ${pet.id}: $error")
                            }
                    }
                    _singleEvent.trySend(FavoriteSingleEvent.AllFavoritesCleared)
                } catch (e: Exception) {
                    Timber.e(e, "Exception while clearing all favorites")
                    _singleEvent.trySend(FavoriteSingleEvent.ToggleLikeError("Failed to clear all favorites"))
                }
            }
        }
    }

    fun navigateToHomeScreen(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(MainDestinations.HOME_ROUTE, MainDestinations.FAVORITE_ROUTE)
    }
}

sealed interface FavoriteUiState {
    data object Loading : FavoriteUiState
    data object Empty : FavoriteUiState
    data class Success(val data: List<Pet>) : FavoriteUiState
    data class Error(val message: String) : FavoriteUiState
}

sealed interface FavoriteSingleEvent {
    data class LoadError(val message: String) : FavoriteSingleEvent
    data class ToggleLikeError(val message: String) : FavoriteSingleEvent
    data class PetRemovedFromFavorites(val petId: String) : FavoriteSingleEvent
    data class PetAddedToFavorites(val petId: String) : FavoriteSingleEvent
    data object AllFavoritesCleared : FavoriteSingleEvent
}