package com.snowaze.app.screens.settings

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.snowaze.app.common.utils.pager.PagerState
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.UserDetail
import com.snowaze.app.screens.SnoWazeViewModel
import com.snowaze.app.screens.onboarding.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService, val application: Application
): SnoWazeViewModel() {

    var uiState = mutableStateOf(SettingsUiState())
        private set
    fun onFirstNameChange(newValue: String) {
        uiState.value = uiState.value.copy(firstName = newValue)
    }

    init {
        launchCatching {
            val userDetail = accountService.getUserData()
            if (userDetail != null) {
                uiState.value = uiState.value.copy(
                    firstName = userDetail.firstName,
                    lastName = userDetail.lastName,
                    pseudo = userDetail.pseudo,
                    skill = userDetail.maxTrackDifficulty
                )
            }
        }
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

    fun onProfileSubmit() {
        uiState.value = uiState.value.copy(hasErrorFirstName = uiState.value.firstName.isEmpty())
        uiState.value = uiState.value.copy(hasErrorLastName = uiState.value.lastName.isEmpty())
        uiState.value = uiState.value.copy(hasErrorPseudo = uiState.value.pseudo.isEmpty())

        if (uiState.value.hasErrorFirstName || uiState.value.hasErrorLastName || uiState.value.hasErrorPseudo) {
            return
        }

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
            uiState.value = uiState.value.copy(isLoading = false)
            uiState.value = uiState.value.copy(isValid = true)
            launchCatching {
                delay(5000)
                uiState.value = uiState.value.copy(isValid = false)
            }
        }
    }

}