package com.snowaze.app.common.utils.comment

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.snowaze.app.R

@Composable
fun ProfilePicture(
    profileImageId: String,
    modifier: Modifier = Modifier,
    size: Dp = ProfilePictureSizes.small
) {
    Image(
        painter = painterResource(id = R.drawable.skier),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
    )
}

object ProfilePictureSizes {
    val small = 32.dp
    val medium = 50.dp
}