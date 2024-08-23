package com.skillcinema.domain

import com.skillcinema.entity.FilmInfo
import javax.inject.Inject

class GetFilmInfoByKinopoiskIdUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     suspend fun execute(kinopoiskId:Int): FilmInfo {
        return repository.getFilmByKinopoiskId(kinopoiskId)
    }
}