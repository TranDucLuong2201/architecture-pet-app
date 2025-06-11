
// VoteUpPetUseCase.kt
package com.dluong.domain.usecase

import com.dluong.designsystem.core.domain.utils.AppError
import com.dluong.domain.model.Pet
import com.dluong.domain.repository.FavoriteCatRepository
import javax.inject.Inject

class VoteUpPetUseCase @Inject constructor(
    private val favoriteCatRepository: FavoriteCatRepository
) {
    /**
     * Vote up a cat (add to favorites).
     *
     * @param cat The cat to vote up.
     * @return A [Result] containing the success or failure of the operation.
     */
    suspend operator fun invoke(cat: Pet): com.dluong.designsystem.core.domain.utils.Result<Unit, AppError> =
        favoriteCatRepository.voteUp(cat)
}