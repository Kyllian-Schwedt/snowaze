package com.snowaze.app.screens.auth.login

import android.widget.VideoView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.snowaze.app.R
import com.snowaze.app.SnoWazeAppState
import com.snowaze.app.compose.EmailField
import com.snowaze.app.compose.PasswordField
import com.snowaze.app.R.string as AppText

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignInClick = { viewModel.onSignInClick(openAndPopUp) },
        onForgotPasswordClick = viewModel::onForgotPasswordClick,
        gotoSignUp = { viewModel.gotoSignUp(openAndPopUp) }
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    gotoSignUp: () -> Unit
) {
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(16.dp, 4.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailField(uiState.email, onEmailChange, fieldModifier)
        PasswordField(uiState.password, onPasswordChange, fieldModifier)

        Button(onClick = { onSignInClick()}) {
            Text(stringResource(id = AppText.sign_in),
            color = Color.White)
        }

        Button(onClick = {
            onForgotPasswordClick()
        }) {
            Text(
                stringResource(id = AppText.forgot_password),
                color = Color.White)
        }

        Button(onClick = {
            gotoSignUp()
        }) {
            Text(stringResource(id = AppText.no_account),
                color = Color.White)
        }

    }
}
