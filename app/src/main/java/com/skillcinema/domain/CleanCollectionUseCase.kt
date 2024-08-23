package com.skillcinema.domain

import javax.inject.Inject

class CleanCollectionUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
    suspend fun execute(collectionName: String) = repository.cleanCollection(collectionName = collectionName)
}