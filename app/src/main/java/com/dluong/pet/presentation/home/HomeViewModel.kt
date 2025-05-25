package com.dluong.pet.presentation.home

import androidx.lifecycle.viewModelScope
import com.dluong.core.domain.utils.onError
import com.dluong.core.domain.utils.onSuccess
import com.dluong.pet.domain.repository.VotePetRepository
import com.dluong.pet.domain.usecase.GetVoteCatsUseCase
import com.dluong.pet.presentation.ui.NetworkStatusViewModel
import com.dluong.pet.presentation.ui.PetsEvent
import com.dluong.pet.presentation.ui.ext.PetsListState
import com.dluong.pet.presentation.ui.ext.SingleEvent
import com.dluong.pet.presentation.ui.ext.UiState
import com.dluong.pet.utils.WHILE_UI_SUBSCRIBED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petsDataSource: VotePetRepository,
) : NetworkStatusViewModel() {

    private val _state = MutableStateFlow(PetsListState())
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = WHILE_UI_SUBSCRIBED,
            initialValue = PetsListState()
        )

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            petsDataSource.getVoteCats(SORT_BY, PAGE_SIZE)
                .onSuccess { pets ->
                    _state.update {
                        it.copy(isLoading = false, pets = pets)
                    }
                    _events.send(PetsEvent.Success("Pets loaded successfully"))
                }
                .onError { error ->
                    _state.update { it.copy(isLoading = false) }
                    _events.send(PetsEvent.Error(error))
                }
        }
    }

    companion object {
        private const val SORT_BY = "RANDOM"
        private const val PAGE_SIZE = 20
    }
}
