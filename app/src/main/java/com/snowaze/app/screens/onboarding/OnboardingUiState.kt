package com.snowaze.app.screens.onboarding

import com.snowaze.app.common.utils.pager.PagerState
import com.snowaze.app.model.Difficulty

data class OnboardingUiState (
    val firstName: String = "",
    val lastName: String = "",
    val pseudo: String = "",
    val skill : Difficulty = Difficulty.BLACK,
    val hasErrorFirstName : Boolean = false,
    val hasErrorLastName : Boolean = false,
    val hasErrorPseudo : Boolean = false,
    val isLoading: Boolean = false,
    val pagerState: PagerState
)