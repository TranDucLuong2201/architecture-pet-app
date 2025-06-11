// VoteDownPetUseCase.kt
package com.dluong.domain.usecase

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.domain.model.Pet
import com.dluong.domain.repository.FavoriteCatRepository
import javax.inject.Inject

class VoteDownPetUseCase @Inject constructor(
    private val favoriteCatRepository: FavoriteCatRepository,
) {
    /**
     * Invokes the use case to vote down a cat (remove from favorites).
     *
     * @param cat The cat to be voted down.
     * @return A result indicating the success or failure of the operation.
     */
    suspend operator fun invoke(cat: Pet): com.dluong.designsystem.core.domain.utils.Result<Unit, AppError> =
        favoriteCatRepository.voteDown(cat)
}