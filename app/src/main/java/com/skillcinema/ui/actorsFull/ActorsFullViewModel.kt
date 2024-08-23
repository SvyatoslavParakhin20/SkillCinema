package com.skillcinema.ui.actorsFull

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skillcinema.domain.GetActorsByKinopoiskIdUseCase
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
class ActorsFullViewModel @Inject constructor(
    state: SavedStateHandle,
    private val getActorsByKinopoiskIdUseCase: GetActorsByKinopoiskIdUseCase
) : ViewModel() {
    private val _actorInfo = MutableStateFlow<List<Any>>(emptyList())
    val actorInfo : StateFlow<List<Any>> = _actorInfo.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _actorInfo.value
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
                val actors = getActorsByKinopoiskIdUseCase.execute(kinopoiskId)
                allInfo[1] = actors.filter { it.professionKey == "ACTOR" }
                allInfo[2] = actors.filter { it.professionKey != "ACTOR" }
                allInfo[0] = true
            }.fold(
                onSuccess = {
                    _actorInfo.value = allInfo.values.toList()
                },
                onFailure = {
                    allInfo[0] = false
                    _actorInfo.value = allInfo.values.toList()
                    Log.d("mytag", "onFailureFilmVM: ${it.message}")
                }
            )
            _isLoading.value = false
        }
    }
}