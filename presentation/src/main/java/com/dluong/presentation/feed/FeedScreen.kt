package com.dluong.presentation.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dluong.presentation.components.CatPostCard
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onNavigateToComments: (String) -> Unit,
    onNavigateToProfile: (String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(uiState.isLoading)

    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        TopAppBar(
            title = { Text("CatTok") }
        )

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.refresh() }
        ) {
            if (uiState.isLoading && uiState.posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.posts) { post ->
                        CatPostCard(
                            post = post,
                            onLikeClick = { viewModel.likePost(it) },
                            onCommentClick = onNavigateToComments,
                            onShareClick = { /* Handle share */ },
                            onUserClick = onNavigateToProfile
                        )
                    }
                }
            }
        }

        // Error Handling
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar or error dialog
            }
        }
    }
}
