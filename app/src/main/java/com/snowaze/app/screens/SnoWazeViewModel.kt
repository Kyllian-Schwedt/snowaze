package com.snowaze.app.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snowaze.app.common.snackbar.SnackbarManager
import com.snowaze.app.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class SnoWazeViewModel : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                throwable.printStackTrace()
            },
            block = block
        )
}