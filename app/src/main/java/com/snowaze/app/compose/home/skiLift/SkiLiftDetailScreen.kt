package com.snowaze.app.compose.home.skiLift

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.snowaze.app.R
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.Comment
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status
import com.snowaze.app.model.TrackService
import com.snowaze.app.ui.theme.md_theme_light_outline
import com.snowaze.app.ui.theme.md_theme_light_primary
import java.util.UUID

@Composable
fun SkiLiftDetailScreen(
    id: UUID,
    navController: NavController,
    viewModel: SkiLiftDetailViewModel = hiltViewModel()
) {
    val trackService: TrackService = viewModel.trackService
    val skiLift = trackService.getSkiLift(id)

    val accountService: AccountService = viewModel.accountService
    val accountId = accountService.currentUserId

    var newCommentText by rememberSaveable { mutableStateOf("") }
    val comments = skiLift?.comments ?: emptyList()

    if (skiLift != null) {
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
                    text = "${skiLift.status}",
                    color = when (skiLift.status) {
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
                            .border(1.dp, Color.Black)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Commentaires",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .heightIn(min = 100.dp, max = 110.dp)
                    ) {
                        if (comments.isEmpty()) {
                            item {
                                Text(
                                    text = "Aucun commentaire pour le moment",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }
                        comments.forEach { comment ->
                            item {
                                CommentRow(comment)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black)
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

