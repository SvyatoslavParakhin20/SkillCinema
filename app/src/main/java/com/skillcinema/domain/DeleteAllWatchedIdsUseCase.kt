package com.skillcinema.domain

import javax.inject.Inject

class DeleteAllWatchedIdsUseCase @Inject constructor(
    private val repository: RepositoryInterface
) {
     suspend fun execute() {
        return repository.deleteAllWatched()
    }
}