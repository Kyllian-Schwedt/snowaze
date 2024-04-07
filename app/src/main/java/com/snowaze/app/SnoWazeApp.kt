package com.snowaze.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.snowaze.app.common.snackbar.SnackbarManager
import com.snowaze.app.compose.navigation.DrawerContent
import com.snowaze.app.compose.navigation.menuItems
import com.snowaze.app.screens.auth.login.LoginScreen
import com.snowaze.app.screens.auth.signup.SignUpScreen
import com.snowaze.app.screens.auth.splash.SplashScreen
import com.snowaze.app.screens.map.MapScreen
import com.snowaze.app.screens.onboarding.OnboardingScreen
import com.snowaze.app.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun SnoWazeApp() {
    AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState(snackbarHostState = SnackbarHostState())
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                        }
                    )
                },
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = androidx.compose.ui.Modifier.padding(innerPaddingModifier)
                ) {
                    snoWazeAppGraph(appState)
                }
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(navController, snackbarManager, resources, coroutineScope) {
    SnoWazeAppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

fun NavGraphBuilder.snoWazeAppGraph(appState: SnoWazeAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(MAIN_APP) { DrawerContent(menuItems = menuItems) }

    composable(ONBOARDING_SCREEN) {
        OnboardingScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }
}