package com.dluong.pet.domain.usecase

import com.dluong.core.domain.utils.NetworkError
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.domain.repository.FavoriteCatRepository
import io.reactivex.rxjava3.core.Observer
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
     * @return An [Observer] that emits the result of the favorite cats.
     */
    operator fun invoke(): Flow<List<Pet>> =
        favoriteCatRepository.observeFavoriteCats()
}