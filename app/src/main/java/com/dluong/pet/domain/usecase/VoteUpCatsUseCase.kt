package com.dluong.pet.domain.usecase

import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
import javax.inject.Inject

class VoteUpCatsUseCase @Inject constructor(
    private val favoriteCatRepository: FavoriteCatRepository
) {
    /**
     * Vote up a cat.
     *
     * @param cat The cat to vote up.
     * @return A [Result] containing the success or failure of the operation.
     */
    suspend operator fun invoke(cat: Pet): Result<Unit> =
        favoriteCatRepository.voteUp(cat)
}