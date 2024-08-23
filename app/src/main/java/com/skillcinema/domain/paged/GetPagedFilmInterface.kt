package com.skillcinema.domain.paged

import com.skillcinema.entity.FilmsDto
import com.skillcinema.data.FilterGenreDto

interface GetPagedFilmInterface {
    suspend fun execute(genre: FilterGenreDto,page:Int): FilmsDto
}