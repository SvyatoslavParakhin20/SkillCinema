package com.skillcinema.domain

import javax.inject.Inject

class DeleteCollectionUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
    suspend fun execute(collectionName: String) = repository.deleteCollection(collection = collectionName)
}