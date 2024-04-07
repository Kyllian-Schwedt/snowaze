package com.snowaze.app.screens.map

import androidx.compose.runtime.snapshots.SnapshotStateList

data class MapUiState (
    val isLoading: Boolean = false
) {
    val markers: SnapshotStateList<ImageMarker> = SnapshotStateList()
}