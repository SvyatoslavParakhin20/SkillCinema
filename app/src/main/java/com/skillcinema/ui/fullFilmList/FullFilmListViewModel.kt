package com.skillcinema.ui.fullFilmList

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.paged.GetPagedPopularUseCase
import com.skillcinema.domain.paged.GetPagedPremiereUseCase
import com.skillcinema.domain.paged.GetPagedRandomGenreFilmsUseCase
import com.skillcinema.domain.paged.GetPagedSeriesUseCase
import com.skillcinema.domain.paged.GetPagedTop250UseCase
import com.skillcinema.entity.FilmDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FullFilmListViewModel @Inject constructor(
    private val getPremiereUseCase: GetPagedPremiereUseCase,
    private val getPopularUseCase: GetPagedPopularUseCase,
    private val getRandomGenreFilmsUseCase: GetPagedRandomGenreFilmsUseCase,
    private val getSeriesUseCase: GetPagedSeriesUseCase,
    private val getTop250UseCase: GetPagedTop250UseCase,
    private val getCollectionFilmIdsUseCase: GetCollectionFilmIdsUseCase
): ViewModel() {
    var filterId = 0
    var category = ""
    val pagedFilm : Flow<PagingData<FilmDto>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {
            when (filterId) {
                1111 -> FilmPagingSource(getPremiereUseCase, category, filterId,getCollectionFilmIdsUseCase)
                2222 -> FilmPagingSource(getPopularUseCase,category,filterId,getCollectionFilmIdsUseCase)
                3333 -> FilmPagingSource(getSeriesUseCase,category,filterId,getCollectionFilmIdsUseCase)
                4444 -> FilmPagingSource(getTop250UseCase,category,filterId,getCollectionFilmIdsUseCase)
                else -> FilmPagingSource(getRandomGenreFilmsUseCase,category,filterId,getCollectionFilmIdsUseCase)
            }
        }
    ).flow
}