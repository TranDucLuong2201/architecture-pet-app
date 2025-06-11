package com.dluong.domain.usecase

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.designsystem.core.domain.utils.Result
import com.dluong.domain.model.Pet
import com.dluong.domain.repository.VotePetRepository
import javax.inject.Inject

/**
 * Use case to get a list of vote cats.
 *
 * @param voteCatRepository The repository to get vote cats from.
 */
class GetVoteCatsUseCase @Inject constructor(
    private val voteCatRepository: VotePetRepository
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
    ): Result<List<Pet>, AppError> =
        voteCatRepository.getVoteCatsUseCase(
            sortBy = sortBy,
            limit = limit,
        )
}