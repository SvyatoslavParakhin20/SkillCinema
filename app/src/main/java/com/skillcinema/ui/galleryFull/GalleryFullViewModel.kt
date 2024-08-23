package com.skillcinema.ui.galleryFull

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skillcinema.domain.GetImagesByKinopoiskIdUseCase
import com.skillcinema.entity.FilmGalleryItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryFullViewModel @Inject constructor(
    private val getImagesByKinopoiskIdUseCase: GetImagesByKinopoiskIdUseCase,
) : ViewModel() {
    var kinopoiskId: Int = 0
    var filterId = ""
    private val _chipInfo = MutableStateFlow<List<Any>>(emptyList())
    val chipInfo: StateFlow<List<Any>> = _chipInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _chipInfo.value
    )
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val allInfo = mutableMapOf<Int, Any>().toSortedMap()
    private val filterList = listOf(
        "STILL",
        "SHOOTING",
        "POSTER",
        "FAN_ART",
        "PROMO",
        "CONCEPT",
        "WALLPAPER",
        "COVER",
        "SCREENSHOT"
    )
    private val chipList = mutableMapOf<String, Int>()
    init {
        _isLoading.value = true
    }
    fun loadChips(kinopoiskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                for (i in filterList) {
                    val response = getImagesByKinopoiskIdUseCase.execute(kinopoiskId, i, 1)
                    if (response.items!!.isNotEmpty()) {
                        chipList[i] = response.total!!
                    }
                }
                allInfo[1] = chipList
                allInfo[0] = true
            }.fold(
                onSuccess = {
                    Log.d("mytag", "OnSuccess Film")
                    _chipInfo.value = allInfo.values.toList()
                },
                onFailure = {
                    allInfo[0] = false
                    _chipInfo.value = allInfo.values.toList()
                    Log.d("mytag", "onFailureFilmVM: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }
    val pagedImages : Flow<PagingData<FilmGalleryItemDto>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = {GalleryPagingSource(getImagesByKinopoiskIdUseCase,kinopoiskId,filterId)
        }
    ).flow
}
