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

    var active by mutableStateOf(false)
        private set

    var isSearchActive by mutableStateOf(false)
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
    }

    fun onSearchActiveChange(active: Boolean) {
        isSearchActive = active
    }
    init {
        launchCatching {
            //TODO fetch configuration
        }
    }
}