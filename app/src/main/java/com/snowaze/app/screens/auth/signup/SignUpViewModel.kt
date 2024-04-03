package com.snowaze.app.screens.auth.signup

import android.app.Application
import android.util.Log
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuthException
import com.snowaze.app.LOGIN_SCREEN
import com.snowaze.app.MAIN_APP
import com.snowaze.app.ONBOARDING_SCREEN
import com.snowaze.app.SIGN_UP_SCREEN
import com.snowaze.app.common.ext.isValidEmail
import com.snowaze.app.common.ext.isValidPassword
import com.snowaze.app.common.ext.passwordMatches
import com.snowaze.app.common.snackbar.SnackbarManager
import com.snowaze.app.model.AccountService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import com.snowaze.app.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService, val application: Application
) : SnoWazeViewModel() {

    private val SIGNUP_TIMEOUT = 1000L

    var uiState = mutableStateOf(SignUpUiState())
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

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {

        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }

        uiState.value = uiState.value.copy(isLoading = true)

        launchCatching {
            accountService.linkAccount(email, password)
            openAndPopUp(ONBOARDING_SCREEN, SIGN_UP_SCREEN)
        }
    }

    fun goToSignIn(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(LOGIN_SCREEN, SIGN_UP_SCREEN)
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
                delay(SIGNUP_TIMEOUT)
                uiState.value = uiState.value.copy(isGoogleLoading = false)
            }
        }
    }

    fun getGso(): GoogleSignInOptions {
        return accountService.getGso()
    }


    private val _signInEvent = MutableLiveData<Unit>()
    val signInEvent: LiveData<Unit> = _signInEvent

    // Call this method when the sign-in button is clicked
    fun onSignInClick() {
        _signInEvent.value = Unit
    }
}