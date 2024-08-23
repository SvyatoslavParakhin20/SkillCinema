package com.skillcinema.domain

import com.skillcinema.entity.FilmSeasonsDto
import javax.inject.Inject

class GetSeasonsByKinopoiskIdUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     suspend fun execute(kinopoiskId:Int): FilmSeasonsDto {
        return repository.getSeasonsByKinopoiskId(kinopoiskId)
    }
}