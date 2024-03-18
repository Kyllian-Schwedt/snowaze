package com.snowaze.app.compose.home

import com.snowaze.app.model.TrackService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val trackService: TrackService
) : SnoWazeViewModel(){

        init {
            launchCatching {
                //TODO fetch configuration
            }
        }
}