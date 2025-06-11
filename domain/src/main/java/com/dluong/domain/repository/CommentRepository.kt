package com.dluong.domain.repository

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.EmptyDataResult
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.Comment
import com.dluong.domain.model.PetPost
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(limit: Int = 20, lastPostId: String? = null): Result<List<PetPost>, AppError>
    suspend fun createPost(imageUrl: String, caption: String): Result<PetPost, AppError>
    suspend fun likePost(postId: String): EmptyDataResult<AppError>
    suspend fun unlikePost(postId: String): EmptyDataResult<AppError>
    suspend fun getComments(postId: String): Result<List<Comment>, AppError>
    suspend fun addComment(postId: String, content: String): Result<Comment, AppError>
    suspend fun deletePost(postId: String): EmptyDataResult<AppError>
    fun getPostsFlow(): Flow<Result<List<PetPost>, AppError>>
}