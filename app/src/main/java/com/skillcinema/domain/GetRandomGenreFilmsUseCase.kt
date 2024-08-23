package com.skillcinema.domain

import com.skillcinema.data.FilterGenreDto
import com.skillcinema.entity.FilmsDto
import javax.inject.Inject

class GetRandomGenreFilmsUseCase @Inject constructor(
    private val repository: RepositoryInterface
){
     suspend fun execute(genre:FilterGenreDto): FilmsDto {
        return repository.getRandomGenreFilms(genre)
    }
}