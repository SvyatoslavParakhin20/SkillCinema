package com.skillcinema.ui.fullCollection

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.CleanCollectionUseCase
import com.skillcinema.domain.GetOneCollectionUseCase
import com.skillcinema.room.CollectionWIthFilms
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullCollectionViewModel @Inject constructor(
    private val getOneCollectionUseCase: GetOneCollectionUseCase,
    private val cleanCollectionUseCase: CleanCollectionUseCase,
) : ViewModel() {
    private val _films = MutableStateFlow<List<CollectionWIthFilms>>(emptyList())
    val films : StateFlow<List<CollectionWIthFilms>> = _films.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _films.value
    )
    fun getFilmsFromDb(collectionName:String) {
        viewModelScope.launch(Dispatchers.IO) {
            var collectionsList = emptyList<CollectionWIthFilms>()
            kotlin.runCatching {
                collectionsList = getOneCollectionUseCase.execute(collectionName = collectionName)
            }.fold(
                onSuccess = {
                    _films.value = collectionsList
                },
                onFailure = { Log.d("mytag", it.message.toString()) }
            )
        }
    }
    fun cleanCollectionHistory(collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                cleanCollectionUseCase.execute(collectionName = collectionName)
            }.fold(
                onSuccess = {  },
                onFailure = { Log.d("mytag", it.message.toString()) })
        }
    }

}