package com.snowaze.app.compose.home.skiLift

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.Status

@Composable
fun SkiliftScreen(SkiList: List<SkiLift>) {
    for (skiLift in SkiList) {
        SkiLiftCard(skiLift)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkiLiftCard(skiLift: SkiLift) {
    OutlinedCard(onClick = { /*TODO*/ },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp),
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
                    modifier = Modifier.padding(bottom = 30.dp),
                    text = skiLift.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                // A color square to represent the difficulty of the track
                Box(
                    modifier = Modifier
                        .requiredWidth(20.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

            }
            //text to display is open in green or closed in red
            Text(
                text = skiLift.status.toString(),
                color = when (skiLift.status) {
                    Status.OPEN -> Color.Green
                    Status.CLOSED -> Color.Red
                    else -> {
                        Color.Gray}
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}