package com.skillcinema.domain

import javax.inject.Inject

class GetCollectionFilmIdsUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     fun execute(collection:String):List<Int> {
        return repository.getFilmIdsFromCollection(collection)
    }
}