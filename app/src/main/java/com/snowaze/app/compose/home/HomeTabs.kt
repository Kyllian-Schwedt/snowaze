package com.snowaze.app.compose.home

import com.snowaze.app.R

enum class HomeTabs(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val text: String
)
{
    Tracks(
        selectedIcon = R.drawable.skiing_icon,
        unselectedIcon = R.drawable.skiing_icon,
        text = "Tracks"
    ),
    SkiLifts(
        selectedIcon = R.drawable.skilift_icon,
        unselectedIcon = R.drawable.skilift_icon,
        text = "Ski Lifts"
    )
}