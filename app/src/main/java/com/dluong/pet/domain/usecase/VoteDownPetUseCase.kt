// VoteDownPetUseCase.kt
package com.dluong.pet.domain.usecase

import com.dluong.core.domain.utils.NetworkError
import com.dluong.core.domain.utils.Result
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
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
    suspend operator fun invoke(cat: Pet): Result<Unit, NetworkError> =
        favoriteCatRepository.voteDown(cat)
}