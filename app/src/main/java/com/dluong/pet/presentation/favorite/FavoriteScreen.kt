
package com.dluong.pet.presentation.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun FavoriteScreen() {
    val viewModel: FavoriteViewModel = hiltViewModel()
    val voteState = viewModel.voteStateFlow.collectAsState(initial = VoteUiState.Loading)


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (val state = voteState.value) {
            is VoteUiState.Loading -> {
                item {
                    Text(text = "Loading...")
                }
            }
            is VoteUiState.Success -> {
                items(state.data) { cat ->
                    AsyncImage(
                        model = cat.urlImage,
                        contentDescription = "Cat Image",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            is VoteUiState.Error -> {
                item {
                    Text(text = "Error: ${state.error ?: "Unknown error"}")
                }
            }
        }
    }
    LaunchedEffect(voteState.value) {
        println("Current vote state: ${voteState.value}")
    }

    LaunchedEffect(Unit) {
        viewModel.getVoteCats()
    }
}

