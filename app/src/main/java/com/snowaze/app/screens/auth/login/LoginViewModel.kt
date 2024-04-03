package com.snowaze.app.screens.auth.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.snowaze.app.LOGIN_SCREEN
import com.snowaze.app.MAIN_APP
import com.snowaze.app.ONBOARDING_SCREEN
import com.snowaze.app.SIGN_UP_SCREEN
import com.snowaze.app.common.ext.isValidEmail
import com.snowaze.app.common.snackbar.SnackbarManager
import com.snowaze.app.model.AccountService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.snowaze.app.R.string as AppText

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService
) : SnoWazeViewModel() {
    private val SIGNIN_TIMEOUT = 1000L
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
            openAndPopUp(ONBOARDING_SCREEN, SIGN_UP_SCREEN)
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

    fun authenticateWithGoogle(idToken: String?, openAndPopUp: (String, String) -> Unit) {
        Log.d("SignUpScreen", "Google sign-in loading vm")
        if(idToken == null) {
            Log.e("SignUpViewModel", "Google sign-in failed: idToken is null")
            return
        }
        uiState.value = uiState.value.copy(isGoogleLoading = true)
        launchCatching {
            try {
                Log.d("SignUpViewModel", "Google sign-in clicked")
                accountService.googleSignIn(idToken)
                openAndPopUp(ONBOARDING_SCREEN, SIGN_UP_SCREEN)
            } finally {
                delay(SIGNIN_TIMEOUT)
                uiState.value = uiState.value.copy(isGoogleLoading = false)
            }
        }
    }

    fun getGso(): GoogleSignInOptions {
        return accountService.getGso()
    }

    fun gotoSignUp(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(SIGN_UP_SCREEN, LOGIN_SCREEN)
        }
    }
}