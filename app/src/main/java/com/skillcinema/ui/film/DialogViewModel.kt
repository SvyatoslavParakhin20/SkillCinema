package com.skillcinema.ui.film

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.DeleteFilmFromCollectionUseCase
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.GetFullCollectionsUseCase
import com.skillcinema.domain.InsertCollectionUseCase
import com.skillcinema.domain.InsertFilmToDbUseCase
import com.skillcinema.entity.FilmInfo
import com.skillcinema.room.Collection
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Collections
import com.skillcinema.room.Film
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.N)
@HiltViewModel
class DialogViewModel @Inject constructor(
    val state:SavedStateHandle,
    private val insertCollectionUseCase: InsertCollectionUseCase,
    private val getFullCollectionsUseCase: GetFullCollectionsUseCase,
    private val insertFilmToDbUseCase: InsertFilmToDbUseCase,
    private val deleteFilmFromCollectionUseCase: DeleteFilmFromCollectionUseCase,
    private val getCollectionFilmIdsUseCase: GetCollectionFilmIdsUseCase,
) : ViewModel() {
    private val _collection = MutableStateFlow<List<CollectionData>>(emptyList())
    val collection = _collection.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _collection.value
    )
    private var filmInfo: FilmInfo = state[FILM_INFO_KEY]!!
    init {
        getCollectionsList()
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCollectionsList() {
        viewModelScope.launch(Dispatchers.IO) {
            val collectionsList = mutableListOf<CollectionData>()
            var collections = emptyList<CollectionWIthFilms>()
            kotlin.runCatching {
                collections = getFullCollectionsUseCase.execute()
            }.fold(
                onSuccess = {
                    for (i in collections) {
                        collectionsList.add(
                            CollectionData(
                                collectionName = i.collection.name,
                                collectionsSize = i.films.size,
                                isInCollection = filmInfo.kinopoiskId in getCollectionFilmIdsUseCase.execute(i.collection.name)
                            )
                        )
                        collectionsList.removeIf { it.collectionName == Collections.HISTORY.rusName }
                    }
                    _collection.value =collectionsList.toList()
                },
                onFailure = { Log.d("mytag", it.message.toString()) }
            )
        }
    }
    fun addToCollections(checkedCollection: String,filmInfo: FilmInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                    insertFilmToDbUseCase.execute(
                        collection = checkedCollection, film = Film(
                            kinopoiskId = filmInfo.kinopoiskId!!,
                            duration = filmInfo.filmLength,
                            nameEn = filmInfo.nameEn as String?,
                            nameRu = filmInfo.nameRu,
                            posterUrl = filmInfo.posterUrl,
                            posterUrlPreview = filmInfo.posterUrlPreview,
                            premiereRu = null,
                            year = filmInfo.year,
                            ratingKinopoisk = filmInfo.ratingKinopoisk,
                            filmId = filmInfo.kinopoiskId,
                            isWatched = false,
                            countries = filmInfo.countries.joinToString(", ") { it.country.toString() },
                            genres = filmInfo.genres.joinToString(", ") { it.genre.toString() },
                            isLiked = false,
                            toWatch = false
                        )
                    )
            }.fold(
                onSuccess = {
                    getCollectionsList()
                },
                onFailure = { Log.d("mytag", it.message.toString()) }
            )
        }
    }
    fun createCollection(collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            insertCollectionUseCase.execute(collection = Collection(collectionName))
        }
    }
    fun deleteFromCollection(collection: String, filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                deleteFilmFromCollectionUseCase.execute(collection = collection, filmId = filmId)
            }.fold(
                onSuccess = {
                    getCollectionsList()
                }, onFailure = {Log.d("mytag", it.message.toString())}
            )
        }
    }
    companion object{
        private const val FILM_INFO_KEY = "filmInfo"
    }
}
