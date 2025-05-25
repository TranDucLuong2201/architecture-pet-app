package com.dluong.pet.presentation.ui.ext

// Compare this snippet from app/src/main/java/com/rxmobileteam/pet/presentation/ui/ext/UiState.kt:
sealed interface UiState {
    data object Loading : UiState

    data class Success<T>(val data: T) : UiState

    data class Error(val message: String) : UiState

}

// Compare this snippet from app/src/main/java/com/rxmobileteam/pet/presentation/ui/ext/SingleEvent.kt:
sealed interface SingleEvent {
    data class GetListError(val throwable: Throwable) : UiState
    data object Error : UiState
}