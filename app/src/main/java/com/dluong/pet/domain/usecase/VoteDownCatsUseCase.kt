package com.dluong.pet.domain.usecase

import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
import javax.inject.Inject

class VoteDownCatsUseCase @Inject constructor(
    private val favoriteCatRepository: FavoriteCatRepository,
) {
    /**
     * Invokes the use case to vote down a cat.
     *
     * @param cat The cat to be voted down.
     * @return A result indicating the success or failure of the operation.
     */
    suspend operator fun invoke(cat: Pet): Result<Unit> =
        favoriteCatRepository.voteDown(cat)
}