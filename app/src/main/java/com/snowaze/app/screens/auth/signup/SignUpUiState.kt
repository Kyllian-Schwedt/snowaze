package com.snowaze.app.screens.auth.signup

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val hasErrorEmail : Boolean = false,
    val hasErrorPass: Boolean = false,
    val hasErrorRepeatPass: Boolean = false,
    val isLoading: Boolean = false,
    val isGoogleLoading: Boolean = false
)