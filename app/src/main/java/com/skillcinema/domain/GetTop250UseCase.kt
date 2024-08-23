package com.skillcinema.domain

import com.skillcinema.entity.FilmsDto
import javax.inject.Inject

class GetTop250UseCase @Inject constructor(
    private val repository: RepositoryInterface
){
    suspend fun execute(): FilmsDto {
        return repository.getTop250()
    }
}