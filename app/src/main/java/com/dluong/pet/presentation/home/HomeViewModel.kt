package com.dluong.pet.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dluong.pet.domain.model.Cat
import com.dluong.pet.domain.usecase.GetVoteCatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    val getVoteCatsUseCase: GetVoteCatsUseCase,
) : ViewModel() {
    /**
     * StateFlow to hold the current state of the vote.
     * It starts with a loading state and can be updated to success or error states.
     */
    private val voteMutableStateFlow = MutableStateFlow<VoteUiState>(VoteUiState.Loading)
    val voteStateFlow = voteMutableStateFlow.asStateFlow()

    private val singleEvent = Channel<VoteSingleEvent>(Channel.UNLIMITED)
    val singleEventFlow: Flow<VoteSingleEvent> get() = singleEvent.receiveAsFlow()

    /**
     * Function to fetch the vote cats from the use case.
     * It updates the state flow with the result of the operation.
     */
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

/**
 * Sealed interface representing the UI state of the vote.
 * It can be in a loading state, success state with data, or error state with an error message.
 */
sealed interface VoteUiState {
    data object Loading : VoteUiState

    data class Success(val data: List<Cat>) : VoteUiState

    data class Error(val error: Throwable) : VoteUiState
}

/**
 * Sealed interface representing single events related to voting.
 * It can represent a vote error or an error while fetching the list of votes.
 */
sealed interface VoteSingleEvent {
    data object VoteError : VoteSingleEvent

    data class GetListError(
        val throwable: Throwable,
    ) : VoteSingleEvent
}

