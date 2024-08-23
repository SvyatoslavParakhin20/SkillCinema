package com.skillcinema.ui.similarFull

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.GetSimilarByKinopoiskIdUseCase
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
class SimilarFullViewModel @Inject constructor(
    state: SavedStateHandle,
    private val getSimilarByKinopoiskIdUseCase: GetSimilarByKinopoiskIdUseCase
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
    init {
        val id: Int = state["kinopoiskId"]!!
        loadAll(id)
    }
    private fun loadAll(kinopoiskId:Int) {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                allInfo[1] = getSimilarByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[0] = true

            }.fold(
                onSuccess = {
                    Log.d("mytag", "OnSuccess Film")
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