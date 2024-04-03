package com.snowaze.app.screens.auth.signup

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.snowaze.app.R
import com.snowaze.app.compose.EmailField
import com.snowaze.app.compose.PasswordField
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.snowaze.app.compose.lottie.LottieGoogleLoadingView
import com.snowaze.app.compose.lottie.LottieWorkingLoadingView
import com.snowaze.app.compose.progress.HorizontalDottedProgressBar

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    SignUpScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) },
        goToSignIn = { viewModel.goToSignIn(openAndPopUp) },
        openAndPopUp = openAndPopUp,
        authenticateWithGoogle = viewModel::authenticateWithGoogle,
        getGso = viewModel::getGso
    )
}

@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    goToSignIn: () -> Unit,
    openAndPopUp: (String, String) -> Unit,
    authenticateWithGoogle: (String?, (String, String) -> Unit) -> Unit,
    getGso: () -> GoogleSignInOptions
) {
    Scaffold { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { LottieWorkingLoadingView(context = LocalContext.current) }
            item {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = "Our ski slopes are just waiting for you. Let's start by connecting you!",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            if(uiState.isGoogleLoading) {
                item {
                    Log.d("SignUpScreen", "Google sign-in loading")
                    LottieGoogleLoadingView(context = LocalContext.current)
                }
            } else {

            item {
                EmailField(uiState.email, onEmailChange, uiState.hasErrorEmail)
            }

            item {
                PasswordField(uiState.password, onPasswordChange, uiState.hasErrorPass)
            }

            item {
                PasswordField(uiState.repeatPassword, onRepeatPasswordChange, uiState.hasErrorRepeatPass)
            }

            item {
                Button(
                    onClick = {
                        onSignUpClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                ) {
                    if (uiState.isLoading) {
                        HorizontalDottedProgressBar()
                    } else {
                        Text(
                            text = stringResource(id = R.string.sign_in),
                            color = Color.White
                        )
                    }
                }
            }
            }

            item {
                Box(modifier = Modifier.padding(vertical = 16.dp)) {
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                    Text(
                        text = "Or use",
                        color = Color.LightGray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                val context = LocalContext.current
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        if (account != null) {
                            val idToken = account.idToken
                            //googleSignIn(idToken!!)
                            authenticateWithGoogle(idToken, openAndPopUp)
                        } else {
                            Log.e("SignUpScreen", "Google sign-in failed: account is null")
                        }
                    } catch (e: ApiException) {
                        Log.e("SignUpScreen", "Google sign-in failed: ${e.message} ${e.statusCode} ${e.localizedMessage} ${e.cause}  ${e.stackTraceToString()}")
                    }
                }

                OutlinedButton(
                    onClick = {
                        val gso = getGso()
                        val client = GoogleSignIn.getClient(context, gso)
                        val signInIntent = client.signInIntent
                        launcher.launch(signInIntent)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    FaIcon(
                        faIcon = FaIcons.Google,
                        tint = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                    )
                    Text(
                        text = "Sign in with Google",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 14.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                val primaryColor = MaterialTheme.colorScheme.primary
                val annotatedString = remember {
                    AnnotatedString.Builder("Already have an account? Sign in")
                        .apply {
                            addStyle(style = SpanStyle(color = primaryColor), 24, 32)
                        }
                }
                Text(
                    text = annotatedString.toAnnotatedString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable(onClick = { goToSignIn() }),
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }

        }
    }
}

@Composable
fun GoogleSignInButton(openAndPopUp: (String, String) -> Unit){
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val idToken = account.idToken
                googleSignIn(idToken!!)
            } else {
                Log.e("SignUpScreen", "Google sign-in failed: account is null")
            }
        } catch (e: ApiException) {
            Log.e("SignUpScreen", "Google sign-in failed: ${e.message} ${e.statusCode} ${e.localizedMessage} ${e.cause}  ${e.stackTraceToString()}")
        }
    }

    Button(onClick = {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("TOKEN DANS WEB DANS FIREBASE")
            .build()
        val client = GoogleSignIn.getClient(context, gso)
        val signInIntent = client.signInIntent
        launcher.launch(signInIntent)
    }) {
        Text("Sign in with Google")
    }
}

fun googleSignIn(idToken: String): Unit {
    Log.d("SignUpScreen", "getSignedInAccountFromIntent successful")
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
}