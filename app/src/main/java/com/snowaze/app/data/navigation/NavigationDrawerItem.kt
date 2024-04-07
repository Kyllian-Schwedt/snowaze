package com.snowaze.app.data.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class NavigationItem(
    val route: String,
    val content: @Composable (NavHostController) -> Unit
)
data class NavigationDrawerItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val navigationItem: NavigationItem,
    val displayBottomBar: Boolean = true
    )

