package com.dluong.data.repository

import com.dluong.data.remote.datasource.RemoteUserDataSource
import com.dluong.domain.model.User
import com.dluong.domain.repository.UserRepository

class UserRepositoryImpl(
    private val remoteUserDataSource: RemoteUserDataSource
) : UserRepository {

    override suspend fun signIn(email: String, password: String) =
        remoteUserDataSource.signIn(email, password)

    override suspend fun signUp(email: String, password: String, username: String) =
        remoteUserDataSource.signUp(email, password, username)

    override suspend fun signOut() = remoteUserDataSource.signOut()

    override suspend fun getCurrentUser() = remoteUserDataSource.getCurrentUser()

    override suspend fun updateProfile(user: User) =
        remoteUserDataSource.updateProfile(user)

    override suspend fun followUser(userId: String) =
        remoteUserDataSource.followUser(userId)

    override suspend fun unfollowUser(userId: String) =
        remoteUserDataSource.unfollowUser(userId)

    override fun getCurrentUserFlow() = remoteUserDataSource.getCurrentUserFlow()
}
