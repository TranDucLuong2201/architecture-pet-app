package com.dluong.domain.usecase

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.User
import com.dluong.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String
    ): Result<User, AppError> {
        if (email.isBlank() || password.isBlank() || username.isBlank()) {
            return Result.Error(AppError.Validation.InvalidInput)
        }
        return userRepository.signUp(email, password, username)
    }
}

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User, AppError> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(AppError.Validation.EmptyField)
        }
        return userRepository.signIn(email, password)
    }
}

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User, AppError> = userRepository.getCurrentUser()

    fun flow(): Flow<Result<User?, AppError>> = userRepository.getCurrentUserFlow()
}
