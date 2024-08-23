package com.skillcinema.domain

import com.skillcinema.entity.FilmSimilarsDto
import javax.inject.Inject

class GetSimilarByKinopoiskIdUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     suspend fun execute(kinopoiskId:Int): FilmSimilarsDto {
        return repository.getSimilarByKinopoiskId(kinopoiskId)
    }
}