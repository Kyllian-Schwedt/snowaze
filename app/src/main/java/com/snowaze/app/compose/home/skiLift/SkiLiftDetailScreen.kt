package com.snowaze.app.compose.home.skiLift

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.snowaze.app.R
import com.snowaze.app.common.utils.button.FabItem
import com.snowaze.app.common.utils.button.MultiFloatingActionButton
import com.snowaze.app.common.utils.comment.CommentItem
import com.snowaze.app.compose.home.track.TrackCard
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.Comment
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status
import com.snowaze.app.model.Track
import com.snowaze.app.model.TrackService
import com.snowaze.app.ui.theme.md_theme_light_outline
import com.snowaze.app.ui.theme.md_theme_light_primary
import java.util.UUID

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun SkiLiftDetailScreen(
    id: UUID,
    navController: NavHostController,
    viewModel: SkiLiftDetailViewModel = hiltViewModel()
) {
    val trackService: TrackService = viewModel.trackService
    val skiLift = trackService.getSkiLift(id)

    val accountService: AccountService = viewModel.accountService
    val accountId = accountService.currentUserId

    var newCommentText by rememberSaveable { mutableStateOf("") }

    if (skiLift != null) {
        Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
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
                    ) {
                        Icon(
                            imageVector = when (skiLift.type) {
                                SkiLiftType.CHAIRLIFT -> ImageVector.vectorResource(id = R.drawable.chair_lift_icon)
                                SkiLiftType.GONDOLA -> ImageVector.vectorResource(id = R.drawable.gondola_lift_icon)
                                SkiLiftType.TBAR -> ImageVector.vectorResource(id = R.drawable.noun_ski_lift_8803__1_)
                            },
                            contentDescription = "Ski Lift Icon",
                            tint = Color.Black,
                            modifier = Modifier.fillMaxHeight()
                        )

                    }
                    Text(
                        text = skiLift.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .requiredWidth(20.dp)
                            .height(20.dp)
                            .clip(RoundedCornerShape(4.dp))
                    ) {
                        Icon(
                            imageVector = when (skiLift.type) {
                                SkiLiftType.CHAIRLIFT -> ImageVector.vectorResource(id = R.drawable.chair_lift_icon)
                                SkiLiftType.GONDOLA -> ImageVector.vectorResource(id = R.drawable.gondola_lift_icon)
                                SkiLiftType.TBAR -> ImageVector.vectorResource(id = R.drawable.noun_ski_lift_8803__1_)
                            },
                            contentDescription = "Ski Lift Icon",
                            tint = Color.Black,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
                Text(
                    text = "${skiLift.status.value}",
                    color = when (skiLift.status.value) {
                        Status.OPEN -> Color.Green
                        Status.CLOSED -> Color.Red
                        else -> Color.Black
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            }

            item { Box(modifier = Modifier.padding(top = 64.dp, end = 16.dp, start = 16.dp)) {
                Spacer(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
                Text(
                    text = "Ski slope access",
                    color = Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp)
                )
            } }

            item {
            Box(modifier = Modifier
                .heightIn(max = 300.dp)
                .padding(16.dp)) {
                val tracks = skiLift.hop.filterIsInstance<Track>()
                Log.d("SkiLiftDetailScreen", "tracks: ${tracks.size} / ${skiLift.hop}")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
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
            }

            item {

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
                            Divider(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp))
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 160.dp)
                                .padding(start = 16.dp, end = 16.dp)
                        ) {
                            if (skiLift.comments.value.isEmpty()) {
                                item {
                                    Text(
                                        text = "Aucun commentaire",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 15.dp)
                                    )
                                }
                            } else {
                                itemsIndexed(skiLift.comments.value) { index, comment ->
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
                                        trackService.addCommentToSkiLift(
                                            id,
                                            newCommentText,
                                            accountId
                                        )
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

            if(skiLift.status.value != Status.CLOSED) {
                fabItem.add(
                    FabItem(
                        icon = painterResource(id = R.drawable.closed),
                        label = ""
                    ) {
                        trackService.updateSkiLiftStatus(skiLift.id, Status.CLOSED)
                    }
                )
            } else {
                fabItem.add(
                    FabItem(
                        icon = painterResource(id = R.drawable.open_sign),
                        label = ""
                    ) {
                        trackService.updateSkiLiftStatus(skiLift.id, Status.OPEN)
                    }
                )
            }

            MultiFloatingActionButton(
                fabIcon = painterResource(id = R.drawable.warning),
                items = fabItem,
            )
        }
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

