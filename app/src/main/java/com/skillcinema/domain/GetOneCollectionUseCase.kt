package com.skillcinema.domain

import com.skillcinema.room.CollectionWIthFilms
import javax.inject.Inject

class GetOneCollectionUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     fun execute(collectionName: String): List<CollectionWIthFilms> {
        return repository.getOneFullCollection(collectionName = collectionName)
    }
}