package com.skillcinema.domain

import com.skillcinema.entity.ActorGeneralInfoDto
import javax.inject.Inject

class GetActorInfoByKinopoiskIdUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     suspend fun execute(staffId:Int): ActorGeneralInfoDto {
        return repository.getActorInfoByKinopoiskId(staffId)
    }
}