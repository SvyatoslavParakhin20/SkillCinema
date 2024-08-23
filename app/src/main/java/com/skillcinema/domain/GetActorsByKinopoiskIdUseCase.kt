package com.skillcinema.domain

import com.skillcinema.entity.ActorDto
import javax.inject.Inject

class GetActorsByKinopoiskIdUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     suspend fun execute(kinopoiskId:Int): List<ActorDto> {
        return repository.getActorsByKinopoiskId(kinopoiskId)
    }
}