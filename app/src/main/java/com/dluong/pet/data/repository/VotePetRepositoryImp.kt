package com.dluong.pet.data.repository

import com.dluong.core.data.networking.safeCall
import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
import com.dluong.core.domain.utils.map
import com.dluong.pet.data.mapper.toPet
import com.dluong.pet.data.remote.PetService
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.VotePetRepository
import javax.inject.Inject

class VotePetRepositoryImp @Inject constructor(
    private val petService: PetService,
) : VotePetRepository {
    override suspend fun getVoteCatsUseCase(
        sortBy: String,
        limit: Int
    ): Result<List<Pet>, NetworkError> {
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
}