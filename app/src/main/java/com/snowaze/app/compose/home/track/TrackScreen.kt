package com.snowaze.app.compose.home.track

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.snowaze.app.R
import com.snowaze.app.compose.home.HomeViewModel
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.Status
import com.snowaze.app.model.Track


@Composable
fun TrackScreen(viewModel: HomeViewModel, navController: NavHostController) {
    val tracks = viewModel.trackService.tracks
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        itemsIndexed(tracks) { index, track ->
            if(index == 0) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            TrackCard(track = track, navController = navController)
            if (index < tracks.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackCard(track: Track, navController: NavHostController) {
    ElevatedCard(
        modifier = Modifier
            .combinedClickable(
                enabled = true,
                onClick = {
                    navController.navigate("track/${track.id}")
                },
                onLongClick = {
                    /*TODO*/
                }
            )
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 110.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
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
                    text = if (track.section != 0) {
                        "${track.name} - ${track.section}"
                    } else {
                        track.name
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)

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
            IconChip(text = track.difficulty.value, difficulty = track.difficulty, modifier =  Modifier.height(38.dp).requiredWidth(90.dp))

        }
    }
}

@Composable
fun IconChip(
    text: String,
    modifier: Modifier = Modifier,
    difficulty: Difficulty
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(32.dp),
        modifier = modifier
    ) {
        Row(modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.dot_svgrepo_com),
                contentDescription = null,
                modifier = Modifier.padding( 13.dp, 13.dp, 6.dp, 13.dp),
                colorFilter = ColorFilter.tint(when (difficulty) {
                    Difficulty.GREEN -> Color.Green
                    Difficulty.BLUE -> Color.Blue
                    Difficulty.RED -> Color.Red
                    Difficulty.BLACK -> Color.Black
                })
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
            )
        }
    }
}


