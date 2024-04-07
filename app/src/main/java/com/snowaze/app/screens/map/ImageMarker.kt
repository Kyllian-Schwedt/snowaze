package com.snowaze.app.screens.map

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ImageMarker(
    var x: Int,
    var y: Int,
    val data: MutableState<Any?> = mutableStateOf(null),
    var isSelected: MutableState<Boolean> = mutableStateOf(false),
)


