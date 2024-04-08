package com.snowaze.app.compose.navigation

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.snowaze.app.SnoWazeAppState
import com.snowaze.app.compose.home.skiLift.SkiLiftDetailScreen
import com.snowaze.app.compose.home.track.TrackDetailScreen
import com.snowaze.app.data.navigation.NavigationDrawerItem
import com.snowaze.app.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceAsColor")
@Composable
fun DrawerContent(menuItems: List<NavigationDrawerItem>, appState: SnoWazeAppState) {
    AppTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()
        val selectedItemIndex = rememberSaveable { mutableIntStateOf(0) }
        val isDisplayDrawer: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
        LaunchedEffect(navController) {
            navController.currentBackStackEntryFlow.collect { backStackEntry ->
                Log.d("DrawerContent", "currentRoute: ${backStackEntry.destination.route}")
                val currentRoute = backStackEntry.destination.route
                val index = menuItems.indexOfFirst { it.navigationItem.route == currentRoute }

                selectedItemIndex.intValue = if (index == -1) {
                    isDisplayDrawer.value = false
                    0
                } else {
                    isDisplayDrawer.value = true
                    index
                }
            }
        }

        Log.d("DrawerContent", "isDisplayDrawer: ${isDisplayDrawer.value}")

        if(menuItems[selectedItemIndex.intValue].displayBottomBar && isDisplayDrawer.value) {
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet {
                        Spacer(modifier = Modifier.height(16.dp))
                        menuItems.forEachIndexed { index, item ->
                            NavigationDrawerItem(
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.onPrimary,
                                ),
                                label = {
                                    Text(text = item.title)
                                },
                                selected = index == selectedItemIndex.intValue,
                                onClick = {
                                    navController.navigate(item.navigationItem.route)
                                    selectedItemIndex.intValue = index
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex.intValue) {
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
                PageContent(
                    selectedItemIndex = selectedItemIndex,
                    scope = scope,
                    drawerState = drawerState,
                    navController = navController,
                    isDisplayDrawer = isDisplayDrawer,
                    appState = appState
                )
            }
        } else {
            PageContent(
                selectedItemIndex = selectedItemIndex,
                scope = scope,
                drawerState = drawerState,
                navController = navController,
                isDisplayDrawer = isDisplayDrawer,
                appState = appState
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceAsColor")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageContent(
    selectedItemIndex: MutableState<Int>,
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
    isDisplayDrawer: MutableState<Boolean>,
    appState: SnoWazeAppState
){
    Scaffold(
        topBar = {
            if(menuItems[selectedItemIndex.value].displayBottomBar && isDisplayDrawer.value) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                    title = { Text(text = menuItems[selectedItemIndex.value].title) },
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
            } else {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                    title = { Text(text = menuItems[selectedItemIndex.value].title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        },

        bottomBar = {
            if (menuItems[selectedItemIndex.value].displayBottomBar) {
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
                    menuItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                            ),
                            selected = index == selectedItemIndex.value,
                            onClick = {
                                selectedItemIndex.value = index
                                navController.navigate(item.navigationItem.route)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex.value) {
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
        }
    ) { innerPadding ->
        ScreenContent(
            menuItems = menuItems,
            paddingValues = innerPadding,
            navController = navController,
            appState = appState
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScreenContent(
    menuItems: List<NavigationDrawerItem>,
    paddingValues: PaddingValues,
    navController: NavHostController,
    appState: SnoWazeAppState
) {
    Box(
        modifier = Modifier.padding(paddingValues)
    ) {
        NavHost(
            navController = navController,
            startDestination = menuItems.first().navigationItem.route
        ) {
            menuItems.forEach { item ->
                composable(item.navigationItem.route) {
                    item.navigationItem.content(navController, appState)
                }
            }
            composable("track/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val uuid = UUID.fromString(id ?: "")
                TrackDetailScreen(id = uuid, navController = navController)
            }
            composable("skiLift/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val uuid = UUID.fromString(id ?: "")
                SkiLiftDetailScreen(id = uuid, navController = navController)
            }
        }
    }
}
