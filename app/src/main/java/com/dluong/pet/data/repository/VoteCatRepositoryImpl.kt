package com.dluong.pet.data.repository

import com.dluong.pet.data.mapper.toCatDomain
import com.dluong.pet.data.remote.PetService
import com.dluong.pet.domain.model.Cat
import com.dluong.pet.domain.repository.VoteCatRepository
import com.dluong.pet.utils.AppDispatcher
import com.dluong.pet.utils.DispatcherType
import com.dluong.pet.utils.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class VoteCatRepositoryImpl @Inject constructor(
    private val petService: PetService,
    @AppDispatcher(DispatcherType.IO) private val ioDispatcher: CoroutineDispatcher,
) : VoteCatRepository{
    /**
     * Fetches a list of vote able cats from the remote service.
     *
     * @param sortBy The sorting criteria for the cat list.
     * @param limit The maximum number of cats to fetch.
     * @return A Result containing a list of Cat objects or an error.
     */
    override suspend fun getVoteCats(sortBy: String, limit: Int): Result<List<Cat>> {
        return runSuspendCatching(ioDispatcher) {
            petService
                .getVoteList(
                    order = sortBy,
                    limit = limit,
                )
                .map { it.toCatDomain() }
        }
    }
}