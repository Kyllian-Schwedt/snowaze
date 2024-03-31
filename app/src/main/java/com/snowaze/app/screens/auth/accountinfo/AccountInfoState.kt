package com.snowaze.app.screens.auth.accountinfo

import com.snowaze.app.model.Difficulty

data class AccountInfoState (
    val name: String = "",
    val maxTrackDifficulty: Difficulty = Difficulty.BLACK,
    val minTrackDifficulty: Difficulty = Difficulty.GREEN,
    val photoUrl: String = ""
)