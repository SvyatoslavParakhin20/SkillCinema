package com.skillcinema.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.CleanCollectionUseCase
import com.skillcinema.domain.DeleteCollectionUseCase
import com.skillcinema.domain.GetFullCollectionsUseCase
import com.skillcinema.domain.InsertCollectionUseCase
import com.skillcinema.room.Collection
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
class ProfileViewModel @Inject constructor(
    private val getFullCollectionsUseCase: GetFullCollectionsUseCase,
    private val cleanCollectionUseCase: CleanCollectionUseCase,
    private val insertCollectionUseCase: InsertCollectionUseCase,
    private val deleteCollectionUseCase: DeleteCollectionUseCase,
    ): ViewModel() {
    private val _collections = MutableStateFlow<List<CollectionWIthFilms>>(emptyList())
    val collections : StateFlow<List<CollectionWIthFilms>> = _collections.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _collections.value
    )
    fun getCollectionsList() {
        viewModelScope.launch(Dispatchers.IO) {
            var collectionsList = emptyList<CollectionWIthFilms>()
            kotlin.runCatching {
                collectionsList = getFullCollectionsUseCase.execute()
            }.fold(
                onSuccess = {
                    _collections.value = collectionsList
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
    fun createCollection(collectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                insertCollectionUseCase.execute(collection = Collection(collectionName))
            }.fold(
                onSuccess = {},
                onFailure = { Log.d("mytag", it.message.toString()) }
            )
        }
    }
    fun deleteCollection(collectionName: String){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                deleteCollectionUseCase.execute(collectionName)
            }.fold(
                onSuccess = {},
                onFailure = { Log.d("mytag", it.message.toString()) }
            )
        }
    }
}