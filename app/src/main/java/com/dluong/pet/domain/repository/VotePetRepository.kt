package com.dluong.pet.domain.repository

import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
import com.dluong.pet.domain.model.Pet

interface VotePetRepository {
    suspend fun getVoteCats(
        sortBy: String,
        limit: Int,
    ): Result<List<Pet>, NetworkError>

    suspend fun getPets() : Result<List<Pet>, NetworkError>
}