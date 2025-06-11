package com.dluong.data.repository

import com.dluong.data.remote.datasource.RemotePostDataSource
import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.EmptyDataResult
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.Comment
import com.dluong.domain.model.PetPost
import com.dluong.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val remotePostDataSource: RemotePostDataSource
) : PostRepository {
    override suspend fun getPosts(
        limit: Int,
        lastPostId: String?
    ): Result<List<PetPost>, AppError> {
        return remotePostDataSource.getPosts(limit, lastPostId)
    }

    override suspend fun createPost(imageUrl: String, caption: String): Result<PetPost, AppError> {
        return remotePostDataSource.createPost(imageUrl, caption)
    }

    override suspend fun likePost(postId: String): EmptyDataResult<AppError> {
        return remotePostDataSource.likePost(postId)
    }

    override suspend fun unlikePost(postId: String): EmptyDataResult<AppError> {
        return remotePostDataSource.unlikePost(postId)
    }

    override suspend fun getComments(postId: String): Result<List<Comment>, AppError> {
        return remotePostDataSource.getComments(postId)
    }

    override suspend fun addComment(postId: String, content: String): Result<Comment, AppError> {
        return remotePostDataSource.addComment(postId, content)
    }

    override suspend fun deletePost(postId: String): EmptyDataResult<AppError> {
        return remotePostDataSource.deletePost(postId)
    }

    override fun getPostsFlow(): Flow<Result<List<PetPost>, AppError>> {
        return remotePostDataSource.getPostsFlow()
    }
}