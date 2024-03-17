package com.snowaze.app.common.snackbar

import android.util.Log
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackbarManager {
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun showMessage(@StringRes message: Int) {
        Log.d("SnackbarManager", "showMessage$message")
        messages.value = SnackbarMessage.ResourceSnackbar(message)
    }

    fun showMessage(message: SnackbarMessage) {
        messages.value = message
    }

    fun clearSnackbarState() {
        messages.value = null
    }
}