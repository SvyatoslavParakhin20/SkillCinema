package com.skillcinema.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.data.FilmFilters
import com.skillcinema.domain.GetAllCollectionsUseCase
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.GetPopularUseCase
import com.skillcinema.domain.GetPremiereUseCase
import com.skillcinema.domain.GetRandomGenreFilmsUseCase
import com.skillcinema.domain.GetSeriesUseCase
import com.skillcinema.domain.GetSharedPrefsUseCase
import com.skillcinema.domain.GetTop250UseCase
import com.skillcinema.domain.InsertCollectionUseCase
import com.skillcinema.entity.FilmsDto
import com.skillcinema.room.Collection
import com.skillcinema.room.Collections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCollectionFilmIdsUseCase: GetCollectionFilmIdsUseCase,
    private val getAllCollectionsUseCase: GetAllCollectionsUseCase,
    private val insertCollectionUseCase: InsertCollectionUseCase,
    private val getPremiereUseCase: GetPremiereUseCase,
    private val getSharedPrefsUseCase: GetSharedPrefsUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getSeriesUseCase: GetSeriesUseCase,
    private val getTop250UseCase: GetTop250UseCase,
    private val getRandomGenreFilmsUseCase: GetRandomGenreFilmsUseCase,
    ) : ViewModel() {
    var onboardingShownFlag: Int = 0
    private val _movies = MutableStateFlow<List<FilmsDto>>(emptyList())
    val movies : StateFlow<List<FilmsDto>> = _movies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _movies.value
    )
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()
    private val allFilms = mutableMapOf<Int, FilmsDto>().toSortedMap()
    private var watchedIds = emptyList<Int>()
    init {
        dbJob()
        loadAll()
    }
    fun refresh() {
        dbJob()
        loadAll()
    }
    fun checkForOnboarding() {
        viewModelScope.launch {
            onboardingShownFlag = getSharedPrefsUseCase.execute()
        }
    }
    private fun dbJob() {
        viewModelScope.launch(Dispatchers.IO) {
            val collections = getAllCollectionsUseCase.execute()
            if (collections.isEmpty()) {
                insertCollectionUseCase.execute(Collection(name = Collections.WATCHED_LIST.rusName))
                insertCollectionUseCase.execute(Collection(name = Collections.LIKED.rusName))
                insertCollectionUseCase.execute(Collection(name = Collections.TO_WATCH.rusName))
                insertCollectionUseCase.execute(Collection(name = Collections.HISTORY.rusName))
            }
        }
    }
    fun checkWatched() {
        viewModelScope.launch(Dispatchers.IO) {
        watchedIds = getCollectionFilmIdsUseCase.execute (Collections.WATCHED_LIST.rusName)
        for (filmList in allFilms) {
            filmList.value.items?.forEach { it.isWatched = it.kinopoiskId in watchedIds }
        }
        }
    }
     private fun loadAll() {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                allFilms[1] = getPremiereUseCase.execute()
                allFilms[2] = getPopularUseCase.execute()
                allFilms[3] = getRandomGenreFilmsUseCase.execute(FilmFilters.getRandomGenre())
                allFilms[4] = getRandomGenreFilmsUseCase.execute(FilmFilters.getRandomGenre())
                allFilms[5] = getRandomGenreFilmsUseCase.execute(FilmFilters.getRandomGenre())
                allFilms[6] = getTop250UseCase.execute()
                allFilms[7] = getSeriesUseCase.execute()
            }.fold(
                onSuccess = {
                    allFilms.forEach { it.value.items = it.value.items!!.shuffled() }
                    _movies.value = allFilms.values.toList()
                },
                onFailure = {
                    Log.d("mytag", "onFailure: ${it.message}")
                    _isError.value = true
                }
            )
            _isLoading.value = false
        }
    }
}