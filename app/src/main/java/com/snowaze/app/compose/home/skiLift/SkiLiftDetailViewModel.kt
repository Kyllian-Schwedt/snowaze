package com.snowaze.app.compose.home.skiLift

import com.snowaze.app.model.AccountService
import com.snowaze.app.model.TrackService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SkiLiftDetailViewModel  @Inject constructor(
    val trackService: TrackService,
    val accountService: AccountService
) : SnoWazeViewModel() {
    init {
        launchCatching {
            //TODO fetch configuration
        }
    }
}