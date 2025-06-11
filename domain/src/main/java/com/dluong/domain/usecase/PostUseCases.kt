package com.dluong.domain.usecase

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.EmptyDataResult
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.Comment
import com.dluong.domain.model.PetPost
import com.dluong.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(limit: Int = 20, lastPostId: String? = null): Result<List<PetPost>, AppError> {
        return postRepository.getPosts(limit, lastPostId)
    }

    fun flow(): Flow<Result<List<PetPost>, AppError>> = postRepository.getPostsFlow()
}

class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(imageUrl: String, caption: String): Result<PetPost, AppError> {
        if (imageUrl.isBlank()) {
            return Result.Error(AppError.Validation.EmptyField)
        }
        return postRepository.createPost(imageUrl, caption)
    }
}

class LikePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String): EmptyDataResult<AppError> {
        return postRepository.likePost(postId)
    }
}

class AddCommentUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postId: String, content: String): Result<Comment, AppError> {
        if (content.isBlank()) {
            return Result.Error(AppError.Validation.EmptyField)
        }
        return postRepository.addComment(postId, content)
    }
}
