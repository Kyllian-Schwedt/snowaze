package com.snowaze.app.screens.auth.splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuthException
import com.snowaze.app.MAIN_APP
import com.snowaze.app.SIGN_UP_SCREEN
import com.snowaze.app.SPLASH_SCREEN
import com.snowaze.app.model.AccountService
import com.snowaze.app.screens.SnoWazeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountService: AccountService
) : SnoWazeViewModel() {
    val showError = mutableStateOf(false)

    init {
        launchCatching {
            //TODO fetch configuration
        }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        if (accountService.hasUser) openAndPopUp(MAIN_APP, SPLASH_SCREEN)
        else createAnonymousAccount(openAndPopUp)
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        Log.d("SplashViewModel", "createAnonymousAccount")
        launchCatching(snackbar = false) {
            //For anonymous account creation
            /*try {
                accountService.createAnonymousAccount()
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }*/
            openAndPopUp(SIGN_UP_SCREEN, SPLASH_SCREEN)
        }
    }
}