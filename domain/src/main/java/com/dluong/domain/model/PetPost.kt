package com.dluong.domain.model

data class PetPost(
    val id: String = "",
    val userId: String = "",
    val imageUrl: String = "",
    val caption: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val shareCount: Int = 0,
    val createdAt: Long = 0L,
    val user: User? = null,
    val isLiked: Boolean = false,
    val isFollowed: Boolean = false
)