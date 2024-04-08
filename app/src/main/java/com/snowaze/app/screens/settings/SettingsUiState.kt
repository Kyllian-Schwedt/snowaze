package com.snowaze.app.screens.settings

import com.snowaze.app.common.utils.pager.PagerState
import com.snowaze.app.model.Difficulty

data class SettingsUiState (
    val firstName: String = "",
    val lastName: String = "",
    val pseudo: String = "",
    val skill : Difficulty = Difficulty.BLACK,
    val hasErrorFirstName : Boolean = false,
    val hasErrorLastName : Boolean = false,
    val hasErrorPseudo : Boolean = false,
    val isLoading: Boolean = false,
    val isValid: Boolean = false
)