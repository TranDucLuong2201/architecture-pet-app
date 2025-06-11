package com.dluong.data.remote.datasource

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.EmptyDataResult
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteUserDataSource {
    suspend fun signUp(email: String, password: String, username: String): Result<User, AppError>
    suspend fun signIn(email: String, password: String): Result<User, AppError>
    suspend fun signOut(): EmptyDataResult<AppError>
    suspend fun getCurrentUser(): Result<User, AppError>
    suspend fun updateProfile(user: User): Result<User, AppError>
    suspend fun followUser(userId: String): EmptyDataResult<AppError>
    suspend fun unfollowUser(userId: String): EmptyDataResult<AppError>
    fun getCurrentUserFlow(): Flow<Result<User?, AppError>>
}
