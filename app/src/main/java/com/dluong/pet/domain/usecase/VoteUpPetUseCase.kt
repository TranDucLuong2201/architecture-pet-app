
// VoteUpPetUseCase.kt
package com.dluong.pet.domain.usecase

import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
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
    suspend operator fun invoke(cat: Pet): Result<Unit, NetworkError> =
        favoriteCatRepository.voteUp(cat)
}