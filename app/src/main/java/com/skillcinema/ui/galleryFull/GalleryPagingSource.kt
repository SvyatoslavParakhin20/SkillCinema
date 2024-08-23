package com.skillcinema.ui.galleryFull

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skillcinema.domain.GetImagesByKinopoiskIdUseCase
import com.skillcinema.entity.FilmGalleryItemDto
import javax.inject.Inject

class GalleryPagingSource @Inject constructor(
    private val useCase: GetImagesByKinopoiskIdUseCase,
    private val kinopoiskId: Int,
    private val type:String
) : PagingSource<Int, FilmGalleryItemDto>() {
    override fun getRefreshKey(state: PagingState<Int, FilmGalleryItemDto>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmGalleryItemDto> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            useCase.execute(kinopoiskId,type,page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.items!!,
                    prevKey = null,
                    nextKey =   if(it.items.isEmpty())null else page+1
                )
            }, onFailure = {
                Log.d("mytag","pagingSourceOnFailure: $it")
                LoadResult.Error(it)
            }
        )
    }
    private companion object {
        private const val FIRST_PAGE = 1
    }
}