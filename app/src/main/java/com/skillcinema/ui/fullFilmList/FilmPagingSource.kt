package com.skillcinema.ui.fullFilmList

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skillcinema.data.FilterGenreDto
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.paged.GetPagedFilmInterface
import com.skillcinema.entity.FilmDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilmPagingSource (
    private val useCase: GetPagedFilmInterface,
    private val category: String,
    private val filterId:Int,
    private val getCollectionFilmIdsUseCase: GetCollectionFilmIdsUseCase
) : PagingSource<Int, FilmDto>() {
    private lateinit var watchedIdsList: List<Int>
    override fun getRefreshKey(state: PagingState<Int, FilmDto>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmDto> {
        val page = params.key ?: FIRST_PAGE
        checkWatched()
        return kotlin.runCatching {
            useCase.execute(FilterGenreDto(category,filterId),page)
        }.fold(
            onSuccess = { it ->
                Log.d("mytag","onsuccess")
                it.items?.forEach { if (it.kinopoiskId in watchedIdsList) it.isWatched = true }
                LoadResult.Page(
                    data = it.items!!,
                    prevKey = null,
                    nextKey = if (it.category == "Премьеры") null
                    else{
                    if(it.items!!.isEmpty())null else page+1}
                )
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