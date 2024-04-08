package com.snowaze.app.compose.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.snowaze.app.model.TrackService
import com.snowaze.app.ui.theme.md_theme_light_outline
import com.snowaze.app.ui.theme.md_theme_light_primary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val trackService: TrackService = viewModel.trackService
    val chatMessages = trackService.chatMessages
    val accountService = viewModel.accountService
    val accountId = accountService.currentUserId
    val timeFormatter = SimpleDateFormat("HH:mm")
    val dateFormatter = SimpleDateFormat("dd/MM")
    var chatBoxValue by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()

    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(chatMessages) { index, chatMessage ->
                val time = timeFormatter.format(chatMessage.date)
                val date = dateFormatter.format(chatMessage.date)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if ((index > 0 && dateFormatter.format(chatMessages[index - 1].date) != date) || (index == 0))
                        Text(
                            text = date,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(
                                    vertical = 16.dp,
                                )
                        )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (chatMessage.authorId == accountId) Arrangement.End else Arrangement.Start
                ) {
                    Column {
                        val coroutineScope = rememberCoroutineScope()
                        val profileName = remember { mutableStateOf("") }

                        LaunchedEffect(chatMessage.authorId) {
                            coroutineScope.launch {
                                val userData = viewModel.accountService.getAccountInfoById(chatMessage.authorId)
                                profileName.value = userData?.pseudo.orEmpty()
                            }
                        }
                        if ((index > 0 && chatMessages[index - 1].authorId != chatMessage.authorId) || (index == 0))
                            Text(
                                text = (profileName.value),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .padding(
                                        start = 8.dp,
                                        end = 8.dp,
                                    )
                                    .align(if (chatMessage.authorId == accountId) Alignment.End else Alignment.Start)
                            )
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 4.dp,
                                )
                                .widthIn(max = 300.dp)
                                .align(if (chatMessage.authorId == accountId) Alignment.End else Alignment.Start)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 48f,
                                        topEnd = 48f,
                                        bottomStart = if (chatMessage.authorId == accountId) 48f else 0f,
                                        bottomEnd = if (chatMessage.authorId == accountId) 0f else 48f
                                    )
                                )
                                .background(
                                    if (chatMessage.authorId == accountId) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = chatMessage.text,
                                color = MaterialTheme.colorScheme.primaryContainer
                            )
                        }
                        //display time
                        if ((index < chatMessages.size - 1 && timeFormatter.format(chatMessages[index + 1].date) != time) || index == chatMessages.size - 1) {
                            Text(
                                text = time,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .padding(
                                        start = 8.dp,
                                        end = 8.dp,
                                        top = 4.dp,
                                        bottom = 4.dp
                                    )
                                    .align(if (chatMessage.authorId == accountId) Alignment.End else Alignment.Start)
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            TextField(
                value = chatBoxValue,
                onValueChange = { chatBoxValue = it },
                placeholder = { Text("Your message...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
            )
            IconButton(
                onClick = {
                    trackService.addChatMessage(chatBoxValue, accountId)
                    chatBoxValue = ""
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = md_theme_light_primary,
                    disabledContentColor = md_theme_light_outline
                ),
                enabled = chatBoxValue.isNotEmpty(),
            ) {
                Icon(Icons.Outlined.Send, contentDescription = "Send")
            }
        }
    }

}
