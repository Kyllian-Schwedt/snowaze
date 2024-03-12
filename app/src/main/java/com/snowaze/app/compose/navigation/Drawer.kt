package com.snowaze.app.compose.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.snowaze.app.R
import com.snowaze.app.compose.home.HomeScreen
import com.snowaze.app.compose.itinerary.ItineraryScreen
import com.snowaze.app.compose.settings.SettingsScreen
import com.snowaze.app.data.navigation.NavigationDrawerItem
import com.snowaze.app.ui.theme.AppTheme
import kotlinx.coroutines.launch


val items = listOf(
    NavigationDrawerItem(
        title = "Home",
        selectedIcon = Icons.Outlined.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = "home",
    ) { navController: NavHostController -> HomeScreen(navController) },
    NavigationDrawerItem(
        title = "Itinerary",
        selectedIcon = Icons.Outlined.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
        badgeCount = 0,
        route = "itinerary",
    ) { navController: NavHostController -> ItineraryScreen(navController) },
        NavigationDrawerItem(
            title = "Settings",
            selectedIcon = Icons.Outlined.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "settings",
        ) { navController: NavHostController -> SettingsScreen(navController) }
)
        @SuppressLint("ResourceAsColor")
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun DrawerContent(items: List<NavigationDrawerItem>) {
            AppTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Spacer(modifier = Modifier.height(16.dp))
                            items.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = MaterialTheme.colorScheme.onPrimary,
                                    ),
                                    label = {
                                        Text(text = item.title)
                                    },
                                    selected = index == selectedItemIndex,
                                    onClick = {
                                        navController.navigate(item.route)
                                        selectedItemIndex = index
                                        scope.launch {
                                            drawerState.close()
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) {
                                                item.selectedIcon
                                            } else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    },
                                    badge = {
                                        item.badgeCount?.let {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    },
                    drawerState = drawerState,
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                                title = { Text(text = items[selectedItemIndex].title) },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar(
                                modifier = Modifier
                                    .drawBehind {
                                        val borderSize = 1.dp.toPx()
                                        drawLine(
                                            color = Color(R.color.black),
                                            start = Offset(0f, 0f),
                                            end = Offset(size.width, 0f),
                                            strokeWidth = borderSize
                                        )
                                    },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                            ) {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                        ),
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            selectedItemIndex = index
                                            navController.navigate(item.route)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        },
                                        label = { Text(text = item.title) }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        ScreenContent(
                            items = items,
                            paddingValues = innerPadding,
                            navController = navController,
                        )
                    }
                }
            }
        }

        @Composable
        fun ScreenContent(
            items: List<NavigationDrawerItem>,
            paddingValues: PaddingValues,
            navController: NavHostController,
        ) {
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = items.first().route
                ) {
                    items.forEach { item ->
                        composable(item.route) {
                            item.content(navController)
                        }
                    }
                }

            }
        }
