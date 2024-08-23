package com.skillcinema.ui.film

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.DeleteFilmFromCollectionUseCase
import com.skillcinema.domain.GetActorsByKinopoiskIdUseCase
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.GetFilmInfoByKinopoiskIdUseCase
import com.skillcinema.domain.GetImagesByKinopoiskIdUseCase
import com.skillcinema.domain.GetSeasonsByKinopoiskIdUseCase
import com.skillcinema.domain.GetSimilarByKinopoiskIdUseCase
import com.skillcinema.domain.InsertFilmToDbUseCase
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.room.Collections
import com.skillcinema.room.Film
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
class FilmViewModel @Inject constructor(
    state: SavedStateHandle,
    private val insertFilmToDbUseCase: InsertFilmToDbUseCase,
    private val deleteFilmFromCollectionUseCase: DeleteFilmFromCollectionUseCase,
    private val getCollectionFilmIdsUseCase: GetCollectionFilmIdsUseCase,
    private val getFilmInfoByKinopoiskIdUseCase: GetFilmInfoByKinopoiskIdUseCase,
    private val getActorsByKinopoiskIdUseCase: GetActorsByKinopoiskIdUseCase,
    private val getImagesByKinopoiskIdUseCase: GetImagesByKinopoiskIdUseCase,
    private val getSimilarByKinopoiskIdUseCase: GetSimilarByKinopoiskIdUseCase,
    private val getSeasonsByKinopoiskIdUseCase: GetSeasonsByKinopoiskIdUseCase
) : ViewModel() {
    private val _movieInfo = MutableStateFlow<List<Any>>(emptyList())
    val movieInfo : StateFlow<List<Any>> = _movieInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _movieInfo.value
    )
    private val allInfo = mutableMapOf<Int, Any>().toSortedMap()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private var watchedIds = listOf<Int>()
    private var likedIds = listOf<Int>()
    private var toWatchIds = listOf<Int>()
    init {
        val id:Int = state["kinopoiskId"]!!
        loadAll(id)
        checkHistory()
    }
    fun checkWatched() {
        viewModelScope.launch(Dispatchers.IO) {
            watchedIds = getCollectionFilmIdsUseCase.execute (Collections.WATCHED_LIST.rusName)
            (allInfo[1] as FilmInfo).isWatched =
                (allInfo[1] as FilmInfo).kinopoiskId in watchedIds
            for (item in (allInfo[6] as FilmSimilarsDto).items!!){
                item.isWatched = item.filmId in watchedIds
            }
        }
    }
    fun checkLiked() {
        viewModelScope.launch(Dispatchers.IO) {
            likedIds = getCollectionFilmIdsUseCase.execute (Collections.LIKED.rusName)
            (allInfo[1] as FilmInfo).isLiked =
                (allInfo[1] as FilmInfo).kinopoiskId in likedIds
        }
    }
    fun checkToWatch() {
        viewModelScope.launch(Dispatchers.IO) {
            toWatchIds = getCollectionFilmIdsUseCase.execute (Collections.TO_WATCH.rusName)
            (allInfo[1] as FilmInfo).toWatch =
                (allInfo[1] as FilmInfo).kinopoiskId in toWatchIds
        }
    }
    private fun checkHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history =
                getCollectionFilmIdsUseCase.execute(collection = Collections.HISTORY.rusName)
                    .toMutableList()
            while (history.size > 100) {
                deleteFilmFromCollectionUseCase.execute(
                    collection = Collections.HISTORY.rusName, filmId = history.first()
                )
                history.removeFirst()
            }
        }
    }
    fun addFilmToCollection(collection: String,film: Film){
        viewModelScope.launch(Dispatchers.IO) {
            insertFilmToDbUseCase.execute(collection = collection, film = film)
        }
    }
    fun deleteFilmFromCollection(filmId:Int,collection:String){
        viewModelScope.launch (Dispatchers.IO) {
        deleteFilmFromCollectionUseCase.execute(collection = collection, filmId = filmId)
        }
    }
    private fun loadAll(kinopoiskId:Int) {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                allInfo[1] = getFilmInfoByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[2] = getSeasonsByKinopoiskIdUseCase.execute(kinopoiskId)
                val actors = getActorsByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[3] = actors.filter { it.professionKey == "ACTOR" }
                allInfo[4] = actors.filter { it.professionKey != "ACTOR" }
                val images = when{
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"STILL",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"STILL",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SHOOTING",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SHOOTING",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"POSTER",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"POSTER",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"FAN_ART",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"FAN_ART",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"PROMO",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"PROMO",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"CONCEPT",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"CONCEPT",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"WALLPAPER",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"WALLPAPER",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"COVER",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"COVER",1)
                    getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SCREENSHOT",1).items!!.isNotEmpty() -> getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"SCREENSHOT",1)
                    else -> {getImagesByKinopoiskIdUseCase.execute(kinopoiskId,"STILL",1)}
                }
                allInfo[5] = images
                allInfo[6] = getSimilarByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[0] = true
            }.fold(
                onSuccess = {
                    _movieInfo.value = allInfo.values.toList()
                },
                onFailure = {
                    allInfo[0] = false
                    _movieInfo.value = allInfo.values.toList()
                    Log.d("mytag", "onFailureFilmVM: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }
}