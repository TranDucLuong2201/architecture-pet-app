package com.dluong.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
}
