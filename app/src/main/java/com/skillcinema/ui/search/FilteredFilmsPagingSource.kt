package com.skillcinema.ui.search

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.paged.GetFilmsByFiltersUseCase
import com.skillcinema.entity.FilmDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilteredFilmsPagingSource (
    private val useCase: GetFilmsByFiltersUseCase,
    private var countries: List<Int>?,
    private var genres: List<Int>?,
    private var order: String,
    private var type: String,
    private var ratingFrom: Int,
    private var ratingTo: Int,
    private var yearFrom: Int,
    private var yearTo: Int,
    private var keyword: String,
    private val getCollectionFilmIdsUseCase: GetCollectionFilmIdsUseCase
) : PagingSource<Int, FilmDto>() {
    private lateinit var watchedIdsList: List<Int>
    override fun getRefreshKey(state: PagingState<Int, FilmDto>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDto> {
        val page = params.key ?: FIRST_PAGE
        checkWatched()
        return kotlin.runCatching {
            useCase.execute(
                countries = countries,
                genres =  genres,
                order =  order,
                type = type,
                ratingFrom = ratingFrom,
                ratingTo = ratingTo,
                yearFrom = yearFrom,
                yearTo = yearTo,
                keyword = keyword,
                page = page
                )
        }.fold(
            onSuccess = { it ->
                val notWatchedFilms =  it.items?.filter { it.kinopoiskId !in watchedIdsList }
                Log.d("mytag","onsuccess")
                if (SearchSettings.notWatchedOnly){
                    LoadResult.Page(
                        data = notWatchedFilms!!,
                        prevKey = null,
                        nextKey =
                        if(notWatchedFilms.isEmpty())null else page+1
                    )
                }else{
                    LoadResult.Page(
                        data = it.items!!,
                        prevKey = null,
                        nextKey =
                        if(it.items!!.isEmpty())null else page+1
                    )
                }
            }, onFailure = {
                Log.d("mytag","onfailure")
                LoadResult.Error(it)
            }
        )
    }
    private fun checkWatched() {
        CoroutineScope(Dispatchers.IO).launch {
            watchedIdsList = getCollectionFilmIdsUseCase.execute ("Просмотренное")
        }
    }
    private companion object {
        private const val FIRST_PAGE = 1
    }
}