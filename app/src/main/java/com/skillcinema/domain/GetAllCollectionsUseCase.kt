package com.skillcinema.domain

import com.skillcinema.room.Collection
import javax.inject.Inject

class GetAllCollectionsUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     fun execute(): List<Collection> {
        return repository.getAllCollections()
    }
}