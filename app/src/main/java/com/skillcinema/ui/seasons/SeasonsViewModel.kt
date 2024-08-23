package com.skillcinema.ui.seasons

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.GetSeasonsByKinopoiskIdUseCase
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
class SeasonsViewModel @Inject constructor(
    state: SavedStateHandle,
    private val getSeasonsByKinopoiskIdUseCase: GetSeasonsByKinopoiskIdUseCase
):ViewModel() {
    private val _seasonsInfo = MutableStateFlow<List<Any>>(emptyList())
    val seasonsInfo : StateFlow<List<Any>> = _seasonsInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _seasonsInfo.value
    )
    private val allInfo = mutableMapOf<Int, Any>().toSortedMap()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    init {
        val id:Int = state["kinopoiskId"]!!
        loadAll(id)
    }
    private fun loadAll(kinopoiskId:Int) {
        viewModelScope.launch (Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                allInfo[1] = getSeasonsByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[0] = true
            }.fold(
                onSuccess = {
                    _seasonsInfo.value = allInfo.values.toList()
                },
                onFailure = {
                    allInfo[0] = false
                    _seasonsInfo.value = allInfo.values.toList()
                    Log.d("mytag", "onFailureFilmVM: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }
}

