package com.snowaze.app.compose.lottie

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LottieGoogleLoadingView(context: Context) {
    LottieLoadingView(
        context = context,
        file = "googleLoading.json",
        reverseOnRepeat = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}