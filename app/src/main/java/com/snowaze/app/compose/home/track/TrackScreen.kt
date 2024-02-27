package com.snowaze.app.compose.home.track

import androidx.compose.runtime.Composable
import com.snowaze.app.model.Track

@Composable
fun TrackScreen(tracks: List<Track>) {
    for (track in tracks) {
        TrackCard(track)
    }
}

@Composable
fun TrackCard(track: Track) {

}