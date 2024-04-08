package com.snowaze.app.compose.home.skiLift

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.snowaze.app.compose.home.HomeViewModel
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status

@Composable
fun SkiLiftScreen(viewModel: HomeViewModel, navController: NavHostController) {
    val skiLifts = viewModel.trackService.skiLifts
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        itemsIndexed(skiLifts) { index, skiLift ->
            if(index == 0) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            SkiLiftCard(skiLift = skiLift, navController = navController)
            if (index < skiLifts.size - 1) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SkiLiftCard(skiLift: SkiLift, navController: NavHostController) {
    ElevatedCard(
        modifier = Modifier
            .combinedClickable(
                enabled = true,
                onClick = {
                    navController.navigate("skiLift/${skiLift.id}")
                },
                onLongClick = {
                    /*TODO*/
                }
            )
            .fillMaxWidth()
            .heightIn(min = 120.dp, max = 130.dp),
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
                    .weight(2f)

            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = when (skiLift.type) {
                            SkiLiftType.CHAIRLIFT -> ImageVector.vectorResource(id = R.drawable.chair_lift_icon)
                            SkiLiftType.GONDOLA -> ImageVector.vectorResource(id = R.drawable.gondola_lift_icon)
                            SkiLiftType.TBAR -> ImageVector.vectorResource(id = R.drawable.noun_ski_lift_8803__1_)
                        },
                        contentDescription = "Ski Lift Icon",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(64.dp).padding(end = 32.dp)
                    )
                    Text(
                        text = skiLift.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Text(
                    text = skiLift.status.value.toString(),
                    color = when (skiLift.status.value) {
                        Status.OPEN -> Color.Green
                        Status.CLOSED -> Color.Red
                        Status.UNKNOWN -> Color.Black
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }
        }
    }
}