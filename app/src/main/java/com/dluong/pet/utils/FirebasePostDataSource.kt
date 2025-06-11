package com.dluong.pet.utils

import com.dluong.data.remote.datasource.RemotePostDataSource
import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.EmptyDataResult
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.Comment
import com.dluong.domain.model.PetPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePostDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : RemotePostDataSource {
    override suspend fun getPosts(limit: Int, lastPostId: String?): Result<List<PetPost>, AppError> {
        return try {
            val query = firestore.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())

            val snapshot = query.get().await()
            val posts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(PetPost::class.java)?.copy(id = doc.id)
            }

            Result.Success(posts)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun createPost(imageUrl: String, caption: String): Result<PetPost, AppError> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return Result.Error(AppError.Auth.UserNotFound)

            val post = PetPost(
                userId = userId,
                imageUrl = imageUrl,
                caption = caption,
                createdAt = System.currentTimeMillis()
            )

            val docRef = firestore.collection("posts").add(post).await()
            Result.Success(post.copy(id = docRef.id))
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun likePost(postId: String): EmptyDataResult<AppError> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return Result.Error(AppError.Auth.UserNotFound)
            firestore.collection("likes")
                .document("${userId}_$postId")
                .set(mapOf("userId" to userId, "postId" to postId))
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun unlikePost(postId: String): EmptyDataResult<AppError> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return Result.Error(AppError.Auth.UserNotFound)
            firestore.collection("likes")
                .document("${userId}_$postId")
                .delete()
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun getComments(postId: String): Result<List<Comment>, AppError> {
        return try {
            val snapshot = firestore.collection("comments")
                .whereEqualTo("postId", postId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val comments = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Comment::class.java)?.copy(id = doc.id)
            }

            Result.Success(comments)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun addComment(postId: String, content: String): Result<Comment, AppError> {
        return try {
            val userId = firebaseAuth.currentUser?.uid ?: return Result.Error(AppError.Auth.UserNotFound)

            val comment = Comment(
                postId = postId,
                userId = userId,
                content = content,
                createdAt = System.currentTimeMillis()
            )

            val docRef = firestore.collection("comments").add(comment).await()
            Result.Success(comment.copy(id = docRef.id))
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override suspend fun deletePost(postId: String): EmptyDataResult<AppError> {
        return try {
            firestore.collection("posts").document(postId).delete().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(AppError.Unknown)
        }
    }

    override fun getPostsFlow(): Flow<Result<List<PetPost>, AppError>> = callbackFlow {
        val listener = firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.Error(AppError.Unknown))
                    return@addSnapshotListener
                }

                val posts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(PetPost::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(Result.Success(posts))
            }

        awaitClose { listener.remove() }
    }
}