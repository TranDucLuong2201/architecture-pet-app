package com.dluong.pet.data.repository

import com.dluong.core.data.networking.safeCall
import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
import com.dluong.core.domain.utils.map
import com.dluong.pet.data.mapper.toListPetDto
import com.dluong.pet.data.remote.PetService
import com.dluong.pet.data.remote.response.PetResponse
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.VotePetRepository
import javax.inject.Inject

class VotePetRepositoryImp @Inject constructor(
    private val petService: PetService,
) : VotePetRepository {
    override suspend fun getVoteCats(sortBy: String, limit: Int): Result<List<Pet>, NetworkError> {
        return safeCall<PetResponse> {
            petService.getVoteList(
                order = sortBy,
                limit = limit
            )
        }.map { petResponse ->
            petResponse.toListPetDto()
        }
    }

    override suspend fun getPets(): Result<List<Pet>, NetworkError> {
        TODO("Not yet implemented")
    }
}