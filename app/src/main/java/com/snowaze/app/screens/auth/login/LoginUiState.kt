package com.snowaze.app.screens.auth.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val hasErrorEmail : Boolean = false,
    val hasErrorPass: Boolean = false,
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false
)