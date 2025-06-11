package com.dluong.domain.usecase

import com.dluong.domain.model.Pet
import com.dluong.domain.repository.FavoriteCatRepository
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
     */
    operator fun invoke(): Flow<List<Pet>> =
        favoriteCatRepository.observeFavoriteCats()
}