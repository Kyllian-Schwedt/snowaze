package com.snowaze.app.screens.onboarding

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.snowaze.app.common.utils.pager.PagerState
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.UserDetail
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val accountService: AccountService, val application: Application
): SnoWazeViewModel() {

    val onboardingList = listOf(
        Onboard(
            "Your skier profile",
            "Our application need to know more about you to provide you the best experience",
            OnboardingStep.PROFILE,
            "profile.json",
            btnfunction = { onProfileSubmit() }
        ),
        Onboard(
            "Your skills",
            "Tell us more about your skills to help you find the best ski slopes",
            OnboardingStep.SKILLS,
            "tcSkiing.json",
            btnfunction = { onNextClick() }

        ),
        Onboard(
            "Ready to hit the slopes?",
            "You're all set! Get ready to start accessing the app and soon you'll be skiing down the slopes like a pro!",
            OnboardingStep.ENJOY,
            "enjoy.json",
            btnfunction = { finalSubmit() }
        )
    )
    var uiState = mutableStateOf(OnboardingUiState(pagerState = PagerState(0, 0, onboardingList.size - 1)))
        private set

    private val currentPage
        get() = uiState.value.pagerState.currentPage

    fun onFirstNameChange(newValue: String) {
        uiState.value = uiState.value.copy(firstName = newValue)
    }

    fun onLastNameChange(newValue: String) {
        uiState.value = uiState.value.copy(lastName = newValue)
    }

    fun onPseudoChange(newValue: String) {
        uiState.value = uiState.value.copy(pseudo = newValue)
    }

    fun onSkillChange(newValue: String) {
        uiState.value = uiState.value.copy(skill = Difficulty.valueOf(newValue))
    }

    fun canAccessNextPage(page: Int): Boolean {
        return when (page) {
            0 -> uiState.value.firstName.isNotEmpty() && uiState.value.lastName.isNotEmpty() && uiState.value.pseudo.isNotEmpty()
            else -> true
        }
    }

    fun onProfileSubmit() {
        uiState.value = uiState.value.copy(hasErrorFirstName = uiState.value.firstName.isEmpty())
        uiState.value = uiState.value.copy(hasErrorLastName = uiState.value.lastName.isEmpty())
        uiState.value = uiState.value.copy(hasErrorPseudo = uiState.value.pseudo.isEmpty())

        if (uiState.value.hasErrorFirstName || uiState.value.hasErrorLastName || uiState.value.hasErrorPseudo) {
            return
        }

        onNextClick()
    }

    fun onNextClick() {
        uiState.value = uiState.value.copy(pagerState = uiState.value.pagerState.apply {
            currentPage += 1
        })
    }

    fun finalSubmit() {
        uiState.value = uiState.value.copy(isLoading = true)
        launchCatching {
            accountService.storeUserData(
                UserDetail(
                    firstName = uiState.value.firstName,
                    lastName = uiState.value.lastName,
                    pseudo = uiState.value.pseudo,
                    maxTrackDifficulty = uiState.value.skill,
                )
            )
        }
    }

    fun onPreviousClick() {
        uiState.value = uiState.value.copy(pagerState = uiState.value.pagerState.apply {
            currentPage -= 1
        })
    }



    data class Onboard(val title: String, val description: String, val page: OnboardingStep, val lottieFile: String, val btnfunction: () -> Unit = {})

    enum class OnboardingStep {
        PROFILE,
        SKILLS,
        ENJOY
    }
}