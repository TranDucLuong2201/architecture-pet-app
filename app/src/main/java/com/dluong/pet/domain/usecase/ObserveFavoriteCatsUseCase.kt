package com.dluong.pet.domain.usecase

import com.dluong.pet.domain.model.Cat
import com.dluong.pet.domain.repository.FavoriteCatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing favorite cats.
 *
 * @param favoriteCatRepository The repository for managing favorite cats.
 */
class ObserveFavoriteCatsUseCase @Inject constructor(
    private val favoriteCatRepository: FavoriteCatRepository,
) {
    /**
     * Invokes the use case to observe favorite cats.
     *
     * @return A flow of result containing a list of favorite cats.
     */
    operator fun invoke(): Flow<Result<List<Cat>>> = favoriteCatRepository.observeFavoriteCats()
}