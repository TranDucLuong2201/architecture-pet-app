package com.dluong.domain.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val postCount: Int = 0,
    val createdAt: Long = 0L
)