package com.skillcinema.ui.search

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.paged.GetFilmsByFiltersUseCase
import com.skillcinema.entity.FilmDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getFilmsByFiltersUseCase: GetFilmsByFiltersUseCase,
    private val getCollectionFilmIdsUseCase: GetCollectionFilmIdsUseCase
) : ViewModel() {
    val pagedFilm : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {FilteredFilmsPagingSource(
            getFilmsByFiltersUseCase,
            countries = SearchSettings.countries,
            genres =  SearchSettings.genres,
            order =  SearchSettings.order,
            type = SearchSettings.type,
            ratingFrom = SearchSettings.ratingFrom,
            ratingTo = SearchSettings.ratingTo,
            yearFrom = SearchSettings.yearFrom,
            yearTo = SearchSettings.yearTo,
            keyword = SearchSettings.keyword,
            getCollectionFilmIdsUseCase = getCollectionFilmIdsUseCase
        )}
    ).flow
}