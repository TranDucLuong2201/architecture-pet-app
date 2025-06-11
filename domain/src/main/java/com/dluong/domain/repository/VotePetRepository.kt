package com.dluong.domain.repository

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.Pet

interface VotePetRepository {
    suspend fun getVoteCatsUseCase(
        sortBy: String,
        limit: Int = 20,
    ): Result<List<Pet>, AppError>
    suspend fun getCatById(id: String): Result<Pet, AppError>
}