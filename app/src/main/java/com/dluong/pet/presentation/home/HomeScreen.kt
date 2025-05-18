package com.dluong.pet.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dluong.pet.presentation.favorite.CatItem
import com.dluong.pet.ui.navigation.PetsNavController

@Composable
fun HomeScreen(navController: PetsNavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val voteState = viewModel.voteStateFlow.collectAsState(initial = VoteUiState.Loading)

    LazyRow(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (val state = voteState.value) {
            is VoteUiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxHeight()
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Loading...")
                    }
                }
            }

            is VoteUiState.Success -> {
                items(state.data) { cat ->
                    Box(
                        modifier = Modifier
                            .fillParentMaxHeight()
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CatItem(cat)
                    }
                }
            }

            is VoteUiState.Error -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxHeight()
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "Error: ${state.error.message}")
                    }
                }
            }
        }
    }

    // Trigger fetching data
    LaunchedEffect(Unit) {
        viewModel.getVoteCats()
    }
}
