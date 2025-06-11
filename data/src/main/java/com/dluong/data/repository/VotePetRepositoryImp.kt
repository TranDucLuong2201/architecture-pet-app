package com.dluong.data.repository

import com.dluong.data.mapper.toPet
import com.dluong.data.remote.PetService
import com.dluong.designsystem.core.data.networking.safeCall
import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.designsystem.core.domain.utils.map
import com.dluong.domain.model.Pet
import com.dluong.domain.repository.VotePetRepository
import javax.inject.Inject

class VotePetRepositoryImp @Inject constructor(
    private val petService: PetService,
) : VotePetRepository {
    override suspend fun getVoteCatsUseCase(
        sortBy: String,
        limit: Int
    ): Result<List<Pet>, AppError> {
        return safeCall {
            petService.getVoteList(
                order = sortBy,
                limit = limit
            )
        }.map {
            it.map { petDto ->
                petDto.toPet()
            }
        }
    }

    override suspend fun getCatById(id: String): Result<Pet, AppError> {
        return try {
            val response = petService.getCatById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it.toPet())  // <-- Sửa ở đây
                } ?: Result.Error(AppError.Data.NotFound)
            } else {
                Result.Error(AppError.Network.ApiError(response.code(), response.message()))
            }
        } catch (e: Exception) {
            Result.Error(AppError.Network.Forbidden)
        }
    }

}