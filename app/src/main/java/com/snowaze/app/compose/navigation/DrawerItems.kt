package com.snowaze.app.compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.snowaze.app.SnoWazeAppState
import com.snowaze.app.compose.chat.ChatScreen
import com.snowaze.app.compose.home.HomeScreen
import com.snowaze.app.compose.itinerary.ItineraryScreen
import com.snowaze.app.data.navigation.NavigationDrawerItem
import com.snowaze.app.data.navigation.NavigationItem
import com.snowaze.app.screens.map.MapScreen
import com.snowaze.app.screens.settings.SettingsScreen

val menuItems = listOf(
    NavigationDrawerItem(
        title = "Home",
        selectedIcon = Icons.Outlined.Home,
        unselectedIcon = Icons.Outlined.Home,
        navigationItem = NavigationItem(
            route = "home",
            content = { navController: NavHostController, _ -> HomeScreen(navController, hiltViewModel()) }
        )
    ),
    NavigationDrawerItem(
        title = "Map",
        selectedIcon = Icons.Outlined.Map,
        unselectedIcon = Icons.Outlined.Map,
        navigationItem = NavigationItem(
            route = "map",
            content = { navController: NavHostController, _ -> MapScreen(navController) }
        ),
        displayBottomBar = false
    ),
    NavigationDrawerItem(
        title = "Itinerary",
        selectedIcon = Icons.Outlined.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
        badgeCount = 0,
        navigationItem = NavigationItem(
            route = "itinerary",
            content = { navController: NavHostController, _ -> ItineraryScreen(navController) }
        )
    ),
    NavigationDrawerItem(
        title = "Settings",
        selectedIcon = Icons.Outlined.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        navigationItem = NavigationItem(
            route = "settings",
            content = { _: NavHostController, appState:SnoWazeAppState -> SettingsScreen(appState = appState) }
        )
    ),
    NavigationDrawerItem(
        title = "Chat",
        selectedIcon = Icons.Outlined.MailOutline,
        unselectedIcon = Icons.Outlined.MailOutline,
        navigationItem = NavigationItem(
            route = "chat",
            content = { navController: NavHostController, _ -> ChatScreen(navController, hiltViewModel()) }
        )
    )
)