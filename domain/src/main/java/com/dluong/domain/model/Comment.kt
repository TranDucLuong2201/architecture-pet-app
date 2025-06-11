package com.dluong.domain.model

data class Comment(
    val id: String = "",
    val postId: String = "",
    val userId: String = "",
    val content: String = "",
    val likeCount: Int = 0,
    val createdAt: Long = 0L,
    val user: User? = null,
    val isLiked: Boolean = false
)