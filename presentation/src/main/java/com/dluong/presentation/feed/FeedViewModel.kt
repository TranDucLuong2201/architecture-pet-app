package com.dluong.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dluong.designsystem.core.domain.utils.onError
import com.dluong.designsystem.core.domain.utils.onSuccess
import com.dluong.domain.model.PetPost
import com.dluong.domain.usecase.GetPostsUseCase
import com.dluong.domain.usecase.LikePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val likePostUseCase: LikePostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
        observePosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            getPostsUseCase()
                .onSuccess { posts ->
                    _uiState.value = _uiState.value.copy(
                        posts = posts,
                        isLoading = false,
                        error = null
                    )
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.toString()
                    )
                }
        }
    }

    private fun observePosts() {
        viewModelScope.launch {
            getPostsUseCase.flow().collect { result ->
                result.onSuccess { posts ->
                    _uiState.value = _uiState.value.copy(posts = posts)
                }
            }
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            likePostUseCase(postId)
                .onError { error ->
                    _uiState.value = _uiState.value.copy(error = error.toString())
                }
        }
    }

    fun refresh() {
        loadPosts()
    }
}

data class FeedUiState(
    val posts: List<PetPost> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
