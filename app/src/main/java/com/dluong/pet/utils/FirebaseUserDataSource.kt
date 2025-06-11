package com.dluong.pet.utils

import com.dluong.data.remote.datasource.RemoteUserDataSource
import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.EmptyDataResult
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RemoteUserDataSource {
    override suspend fun signUp(email: String, password: String, username: String): Result<User, AppError> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return Result.Error(AppError.Auth.Unauthenticated)

            val user = User(
                id = userId,
                username = username,
                email = email,
                displayName = username,
                createdAt = System.currentTimeMillis()
            )

            firestore.collection("users").document(userId).set(user).await()
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(AppError.Auth.Unauthenticated)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User, AppError> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return Result.Error(AppError.Auth.Unauthenticated)
            val doc = firestore.collection("users").document(userId).get().await()
            val user = doc.toObject(User::class.java) ?: return Result.Error(AppError.Auth.UserNotFound)
            return Result.Success(user)
        } catch (e: Exception) {
            Result.Error(AppError.Auth.Unauthenticated)
        }
    }

    override suspend fun signOut(): EmptyDataResult<AppError> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun getCurrentUser(): Result<User, AppError> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return Result.Error(AppError.Auth.Unauthenticated)
            val document = firestore.collection("users").document(userId).get().await()
            val user = document.toObject(User::class.java) ?: return Result.Error(AppError.Auth.UserNotFound)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun updateProfile(user: User): Result<User, AppError> {
        return try {
            firestore.collection("users").document(user.id).set(user).await()
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(AppError.Auth.UserNotFound)
        }
    }

    override suspend fun followUser(userId: String): EmptyDataResult<AppError> {
        return try {
            val currentUserId = firebaseAuth.currentUser?.uid ?: return Result.Error(AppError.Auth.Unauthenticated)
            firestore.collection("follows")
                .document("${currentUserId}_$userId")
                .set(mapOf("followerId" to currentUserId, "followingId" to userId))
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun unfollowUser(userId: String): EmptyDataResult<AppError> {
        return try {
            val currentUserId = firebaseAuth.currentUser?.uid ?: return Result.Error(AppError.Auth.Unauthenticated)
            firestore.collection("follows")
                .document("${currentUserId}_$userId")
                .delete()
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override fun getCurrentUserFlow(): Flow<Result<User?, AppError>> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val userId = auth.currentUser?.uid
            if (userId != null) {
                firestore.collection("users").document(userId).get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject(User::class.java)
                        trySend(Result.Success(user))
                    }
                    .addOnFailureListener {
                        trySend(Result.Error(AppError.Unknown))
                    }
            } else {
                trySend(Result.Success(null))
            }
        }

        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

}