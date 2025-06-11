package com.dluong.pet.di

import com.dluong.data.remote.datasource.RemotePostDataSource
import com.dluong.data.remote.datasource.RemoteUserDataSource
import com.dluong.data.repository.PostRepositoryImpl
import com.dluong.data.repository.UserRepositoryImpl
import com.dluong.domain.repository.PostRepository
import com.dluong.domain.repository.UserRepository
import com.dluong.pet.utils.FirebasePostDataSource
import com.dluong.pet.utils.FirebaseUserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideFireStore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideRemoteUserDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RemoteUserDataSource = FirebaseUserDataSource(auth, firestore)

    @Provides
    fun provideRemotePostDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): RemotePostDataSource = FirebasePostDataSource(auth, firestore)

    @Provides
    fun provideUserRepository(
        remoteUserDataSource: RemoteUserDataSource
    ): UserRepository = UserRepositoryImpl(remoteUserDataSource)

    @Provides
    fun providePostRepository(
        remoteUserDataSource: RemotePostDataSource
    ): PostRepository = PostRepositoryImpl(remoteUserDataSource)


}
