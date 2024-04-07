package com.snowaze.app.screens.map

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.snowaze.app.model.Status
import com.snowaze.app.model.TrackService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MapViewModel @Inject constructor(
    val trackService: TrackService
) : SnoWazeViewModel(){
    var uiState = mutableStateOf(MapUiState())
        private set

    var openAndPopUp: ((String, String) -> Unit)? = null

    init {
        trackService.markers.onEach {
            uiState.value = uiState.value.copy(isLoading = false)
        }.launchIn(viewModelScope)
    }

}
