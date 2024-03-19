package com.snowaze.app.compose.home.track

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.Status
import com.snowaze.app.model.Track


@Composable
fun TrackScreen(tracks: List<Track>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(tracks) { index, track ->
            TrackCard(track = track, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TrackCard(track: Track, navController: NavHostController) {
    OutlinedCard(
        modifier = Modifier
            .combinedClickable(
                enabled = true,
                onClick = {
                    /*TODO*/
                },
                onLongClick = {
                    /*TODO*/
                }
            )
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 110.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
           Column(
                modifier = Modifier
                     .weight(1f)

           ) {
                Text(
                    modifier = Modifier.padding(bottom = 15.dp),
                    text = track.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .requiredWidth(20.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            when (track.difficulty) {
                                Difficulty.GREEN -> Color.Green
                                Difficulty.BLUE -> Color.Blue
                                Difficulty.RED -> Color.Red
                                Difficulty.BLACK -> Color.Black
                            }
                        )
                )

           }
            Text(
                text = track.status.value.toString(),
                color = when (track.status.value) {
                    Status.OPEN -> Color.Green
                    Status.CLOSED -> Color.Red
                    else -> {
                        Color.Black}
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


