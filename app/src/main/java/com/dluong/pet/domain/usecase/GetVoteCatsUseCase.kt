package com.dluong.pet.domain.usecase

import com.dluong.pet.domain.model.Cat
import com.dluong.pet.domain.repository.VoteCatRepository
import javax.inject.Inject

/**
 * Use case to get a list of vote cats.
 *
 * @param voteCatRepository The repository to get vote cats from.
 */
class GetVoteCatsUseCase @Inject constructor(
    private val voteCatRepository: VoteCatRepository
) {
    /**
     * Invokes the use case to get a list of vote cats.
     *
     * @param sortBy The sorting criteria for the cat list.
     * @param limit The maximum number of cats to fetch.
     * @return A Result containing a list of Cat objects or an error.
     */
    suspend operator fun invoke(
        sortBy: String,
        limit: Int,
    ): Result<List<Cat>> =
        voteCatRepository.getVoteCats(
            sortBy = sortBy,
            limit = limit,
        )
}