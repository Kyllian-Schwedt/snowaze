package com.snowaze.app.screens.auth.signup

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)