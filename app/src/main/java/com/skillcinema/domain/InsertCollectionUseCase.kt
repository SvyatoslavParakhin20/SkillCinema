package com.skillcinema.domain

import com.skillcinema.room.Collection
import javax.inject.Inject

class InsertCollectionUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
    suspend fun execute(collection: Collection) {
        return repository.insertCollection(collection)
    }
}