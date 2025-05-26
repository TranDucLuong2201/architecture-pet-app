package com.dluong.pet.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.fold
import com.dluong.core.domain.utils.onError
import com.dluong.pet.domain.model.Pet
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
            getVoteCatsUseCase(sortBy = SORT_BY, limit = PAGE_SIZE)
                .fold(
                    onSuccess = { pets ->
                        voteMutableStateFlow.value = VoteUiState.Success(pets)
                    },
                    onFailure = { error ->
                        singleEvent.trySend(VoteSingleEvent.GetListError(error))
                        voteMutableStateFlow.value = VoteUiState.Error(error)
                    }
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

    data class Success(val data: List<Pet>) : VoteUiState

    data class Error(val error: NetworkError) : VoteUiState
}

sealed interface VoteSingleEvent {
    data object VoteError : VoteSingleEvent

    data class GetListError(
        val throwable: NetworkError,
    ) : VoteSingleEvent
}