package com.dluong.pet.domain.repository

import com.dluong.pet.domain.model.Cat

interface VoteCatRepository {
    suspend fun getVoteCats(
        sortBy: String,
        limit: Int,
    ): Result<List<Cat>>
}