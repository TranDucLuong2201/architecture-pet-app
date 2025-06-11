package com.dluong.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dluong.designsystem.core.domain.utils.onError
import com.dluong.designsystem.core.domain.utils.onSuccess
import com.dluong.domain.model.User
import com.dluong.domain.usecase.GetCurrentUserUseCase
import com.dluong.domain.usecase.SignInUseCase
import com.dluong.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            getCurrentUserUseCase.flow().collect { result ->
                result.onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        currentUser = user,
                        isLoggedIn = user != null
                    )
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            signInUseCase(email, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        currentUser = user,
                        isLoggedIn = true,
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

    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            signUpUseCase(email, password, username)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        currentUser = user,
                        isLoggedIn = true,
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
}

data class AuthUiState(
    val currentUser: User? = null,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
