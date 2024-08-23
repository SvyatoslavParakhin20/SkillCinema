package com.skillcinema.domain

import javax.inject.Inject

class DeleteFilmFromCollectionUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
    suspend fun execute(collection: String,filmId:Int) {
        return repository.deleteFilmFromCollection(filmId =  filmId, collection = collection)
    }
}