package com.snowaze.app.common.utils.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.Comment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier,
    authService: AccountService
) {
    val coroutineScope = rememberCoroutineScope()
    val profileName = remember { mutableStateOf("") }

    LaunchedEffect(comment.authorId) {
        coroutineScope.launch {
            val userData = authService.getAccountInfoById(comment.authorId)
            profileName.value = userData?.pseudo.orEmpty()
        }
    }

    val dateFormat = SimpleDateFormat("dd MMMM yy", Locale.ENGLISH)
    val formattedDate = dateFormat.format(comment.date)

    Row(modifier = modifier.padding(8.dp)) {
        ProfilePicture(
            profileImageId = comment.authorId,
            size = ProfilePictureSizes.medium
        )
        Column(modifier = Modifier.padding(start = 8.dp).fillMaxWidth()) {
            ProfileInfo(
                profileName = profileName.value,
                time = formattedDate
            )
            Text(text = comment.text, style = typography.bodyMedium)
            Divider(thickness = 0.5.dp)
        }
    }
}