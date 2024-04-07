package com.snowaze.app.common.utils.comment

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileInfo(
    profileName: String,
    time: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.subtitle1
) {
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = profileName,
            style = textStyle,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " · $time",
            modifier = Modifier.padding(start = 8.dp),
            style = textStyle
        )
    }
}