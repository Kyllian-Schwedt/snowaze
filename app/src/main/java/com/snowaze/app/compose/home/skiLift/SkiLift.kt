package com.snowaze.app.compose.home.skiLift

import androidx.compose.runtime.Composable
import com.snowaze.app.model.SkiLift

@Composable
fun SkiliftScreen(SkiList: List<SkiLift>) {
    for (skiLift in SkiList) {
        SkiLiftCard(skiLift)
    }

}

@Composable
fun SkiLiftCard(skiLift: SkiLift) {

}