package com.snowaze.app.screens.auth.login

import androidx.compose.runtime.mutableStateOf
import com.snowaze.app.LOGIN_SCREEN
import com.snowaze.app.MAIN_APP
import com.snowaze.app.SIGN_UP_SCREEN
import com.snowaze.app.common.ext.isValidEmail
import com.snowaze.app.common.snackbar.SnackbarManager
import com.snowaze.app.model.AccountService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.snowaze.app.R.string as AppText

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService
) : SnoWazeViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(hasErrorEmail = true)
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            uiState.value = uiState.value.copy(hasErrorPass = true)
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        uiState.value = uiState.value.copy(isLoading = true)

        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp(MAIN_APP, SIGN_UP_SCREEN)
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            uiState.value = uiState.value.copy(hasErrorEmail = true)
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage(AppText.recovery_email_sent)
        }
    }

    fun gotoSignUp(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(SIGN_UP_SCREEN, LOGIN_SCREEN)
    }
}