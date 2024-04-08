package com.snowaze.app.compose.home.track

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.snowaze.app.R
import com.snowaze.app.common.utils.button.FabItem
import com.snowaze.app.common.utils.button.MultiFloatingActionButton
import com.snowaze.app.common.utils.comment.CommentItem
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.Comment
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.IPath
import com.snowaze.app.model.Status
import com.snowaze.app.model.Track
import com.snowaze.app.model.TrackService
import com.snowaze.app.ui.theme.md_theme_light_outline
import com.snowaze.app.ui.theme.md_theme_light_primary
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackDetailScreen(
    id: UUID,
    navController: NavHostController,
    viewModel: TrackDetailViewModel = hiltViewModel()
) {
    val trackService: TrackService = viewModel.trackService
    val track = trackService.getTrack(id)

    val accountService: AccountService = viewModel.accountService
    val accountId = accountService.currentUserId
    var newCommentText by rememberSaveable { mutableStateOf("") }


    if (track != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
                    Text(
                        text = track.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
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
                    text = "${track.status.value}",
                    color = when (track.status.value) {
                        Status.OPEN -> Color.Green
                        Status.CLOSED -> Color.Red
                        else -> Color.Black
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Commentaires",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp, top = 16.dp, bottom = 16.dp)
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp, max = 160.dp)
                            .padding(start = 16.dp, end = 16.dp)
                    ) {
                        if (track.comments.value.isEmpty()) {
                            item {
                                Text(
                                    text = "Aucun commentaire",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 15.dp)
                                )
                            }
                        } else {
                            itemsIndexed(track.comments.value) { index, comment ->
                                CommentItem(comment, authService = viewModel.accountService)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextField(
                                value = newCommentText,
                                onValueChange = { newCommentText = it },
                                placeholder = { Text("Ajouter un commentaire") },
                                modifier = Modifier.padding(16.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                            IconButton(
                                onClick = {
                                    trackService.addCommentToTrack(id, newCommentText, accountId)
                                    newCommentText = ""
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = md_theme_light_primary,
                                    disabledContentColor = md_theme_light_outline
                                ),
                                enabled = newCommentText.isNotEmpty(),
                            ) {
                                Icon(Icons.Outlined.Send, contentDescription = "Send")
                            }
                        }
                    }
                }
            }
        }

        val fabItem: MutableList<FabItem> = mutableListOf(/*
            FabItem(
                icon = painterResource(id = R.drawable.ice_cubes),
                label = "Icy ski slope",
                {}),
            FabItem(
                icon = painterResource(id = R.drawable.snowman),
                label = "Melted snow",
                {}),
            FabItem(
                icon = painterResource(id = R.drawable.traffic),
                label = "Heavy traffic",
                {}),*/
        )

        if(track.status.value != Status.CLOSED) {
            fabItem.add(
                FabItem(
                    icon = painterResource(id = R.drawable.closed),
                    label = ""
                ) {
                    trackService.updateTrackStatus(track.id, Status.CLOSED)
                }
            )
        } else {
            fabItem.add(
                FabItem(
                    icon = painterResource(id = R.drawable.open_sign),
                    label = ""
                ) {
                    trackService.updateTrackStatus(track.id, Status.OPEN)
                }
            )
        }

        MultiFloatingActionButton(
            fabIcon = painterResource(id = R.drawable.warning),
            items = fabItem,
        )
    }
}

@Composable
fun CommentRow(comment: Comment) {
    Text(
        text = comment.text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 15.dp)
    )
}
