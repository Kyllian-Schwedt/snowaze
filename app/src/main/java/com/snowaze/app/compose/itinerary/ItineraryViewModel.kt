package com.snowaze.app.compose.itinerary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.IPath
import com.snowaze.app.model.TrackService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    val trackService: TrackService,
    val accountService: AccountService



) : SnoWazeViewModel() {

    private val tracks = trackService.tracks
    private val skiLifts = trackService.skiLifts
    val fullList = tracks + skiLifts
    private val pathsFlow = flowOf(fullList)
    var searchQuery by mutableStateOf("")
        private set

    var isSearchActive by mutableStateOf(false)
        private set

    var isLocked1 by mutableStateOf(false)
        private set

    var isLocked2 by mutableStateOf(false)
        private set


    val searchResults: StateFlow<List<IPath>> =
        snapshotFlow { searchQuery }
            .combine(pathsFlow) { sarchQuery, paths ->
                when {
                    searchQuery.isNotEmpty() -> paths.filter { path ->
                        path.name.contains(searchQuery , ignoreCase = true)
                    }
                    else -> paths
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        onLock1(false)
    }

    fun onSearchActiveChange(active: Boolean) {
        isSearchActive = active
    }


    var searchQuery2 by mutableStateOf("")
        private set

    var isSearchActive2 by mutableStateOf(false)
        private set


    val searchResults2: StateFlow<List<IPath>> =
        snapshotFlow { searchQuery2 }
            .combine(pathsFlow) { sarchQuery, paths ->
                when {
                    searchQuery2.isNotEmpty() -> paths.filter { path ->
                        path.name.contains(searchQuery2 , ignoreCase = true)
                    }
                    else -> paths
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    fun onSearchQueryChange2(query: String) {
        searchQuery2 = query
        onLock2(false)
    }

    fun onSearchActiveChange2(active: Boolean) {
        isSearchActive2 = active
    }

    fun onLock1(locked : Boolean){
        isLocked1 = locked
    }
    fun onLock2(locked : Boolean){
        isLocked2 = locked
    }

    fun isBothLocked() : Boolean{
        return isLocked1 && isLocked2
    }
    init {
        launchCatching {
            //TODO fetch configuration
        }
    }
}