package com.dluong.pet.domain.usecase

import com.dluong.pet.data.repository.FavoriteCatRepositoryImpl
import com.dluong.pet.domain.model.Cat
import javax.inject.Inject

class VoteUpCatsUseCase @Inject constructor(
    private val favoriteCatsUseCase: FavoriteCatRepositoryImpl
) {
    /**
     * Invokes the use case to vote up a cat.
     *
     * @param cat The cat to be voted up.
     * @return A result indicating the success or failure of the operation.
     */
    suspend operator fun invoke(cat: Cat): Result<Unit> =
        favoriteCatsUseCase.voteUp(cat)
}