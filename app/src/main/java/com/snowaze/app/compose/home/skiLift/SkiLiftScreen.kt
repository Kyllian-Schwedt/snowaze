package com.snowaze.app.compose.home.skiLift

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.snowaze.app.R
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status

@Composable
fun SkiLiftScreen(skiLifts: List<SkiLift>, navController: NavHostController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(skiLifts) { index, skiLift ->
            SkiLiftCard(skiLift = skiLift, navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SkiLiftCard(skiLift: SkiLift, navController: NavHostController) {
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
                    text = skiLift.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
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
            //text to display is open in green or closed in red
            Text(
                text = skiLift.status.toString(),
                color = when (skiLift.status) {
                    Status.OPEN -> Color.Green
                    Status.CLOSED -> Color.Red
                    Status.UNKNOWN -> Color.Black
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}