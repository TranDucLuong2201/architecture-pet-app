package com.dluong.pet.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dluong.pet.domain.model.Cat
import com.dluong.pet.domain.usecase.GetVoteCatsUseCase
import com.dluong.pet.domain.usecase.ObserveFavoriteCatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
@Inject
constructor(
    val getVoteCatsUseCase: GetVoteCatsUseCase,
) : ViewModel() {
    private val voteMutableStateFlow = MutableStateFlow<VoteUiState>(VoteUiState.Loading)
    val voteStateFlow = voteMutableStateFlow.asStateFlow()

    private val singleEvent = Channel<VoteSingleEvent>(Channel.UNLIMITED)
    val singleEventFlow: Flow<VoteSingleEvent> get() = singleEvent.receiveAsFlow()

    fun getVoteCats() {
        viewModelScope.launch {
            voteMutableStateFlow.value = getVoteCatsUseCase(sortBy = SORT_BY, limit = PAGE_SIZE)
                .onFailure { throwable ->
                    singleEvent.trySend(
                        VoteSingleEvent.GetListError(
                            throwable,
                        ),
                    )
                }
                .fold(
                    onSuccess = VoteUiState::Success,
                    onFailure = VoteUiState::Error,
                )
        }
    }

    companion object {
        private const val SORT_BY = "RANDOM"
        private const val PAGE_SIZE = 30
    }
}

sealed interface VoteUiState {
    data object Loading : VoteUiState

    data class Success(val data: List<Cat>) : VoteUiState

    data class Error(val error: Throwable) : VoteUiState
}

sealed interface VoteSingleEvent {
    data object VoteError : VoteSingleEvent

    data class GetListError(
        val throwable: Throwable,
    ) : VoteSingleEvent
}

