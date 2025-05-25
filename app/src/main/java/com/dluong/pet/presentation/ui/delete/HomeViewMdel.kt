//package com.dluong.pet.presentation.home
//
//import androidx.lifecycle.viewModelScope
//import com.dluong.core.domain.utils.onError
//import com.dluong.core.domain.utils.onSuccess
//import com.dluong.pet.data.utils.NetworkMonitor
//import com.dluong.pet.domain.repository.VotePetRepository
//import com.dluong.pet.presentation.ui.ext.BaseViewModel
//import com.dluong.pet.presentation.PetsEvent
//import com.dluong.pet.presentation.ViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val petsDataSource: VotePetRepository,
//) : BaseViewModel {
//    /**
//     * ViewModel for managing the state of the pets list.
//     *
//     * @property petsDataSource The repository to fetch pet data.
//     */
//    private fun loadPets() {
//        viewModelScope.launch {
//            _state.update {
//                it.copy(isLoading = true)
//            }
//
//            petsDataSource.getVoteCats(SORT_BY, PAGE_SIZE)
//                .onSuccess { pets ->
//                    _state.update {
//                        it.copy(
//                            isLoading = false,
//                            pets = pets
//                        )
//                    }
//                    _events.send(PetsEvent.Success("Pets loaded successfully"))
//                }
//                .onError { error ->
//                    _state.update { it.copy(isLoading = false) }
//                    _events.send(PetsEvent.Error(error))
//                }
//
//        }
//    }
//}