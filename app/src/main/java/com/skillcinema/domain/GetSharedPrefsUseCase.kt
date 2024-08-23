package com.skillcinema.domain

import javax.inject.Inject

class GetSharedPrefsUseCase @Inject constructor(
private val repository: RepositoryInterface
) {
    fun execute():Int{
        return repository.getOnBoardingFlag()
    }
}