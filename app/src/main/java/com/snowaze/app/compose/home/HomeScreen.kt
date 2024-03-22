package com.snowaze.app.compose.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import com.snowaze.app.compose.home.skiLift.SkiLiftScreen
import com.snowaze.app.compose.home.track.TrackScreen
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { HomeTabs.entries.size })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }
    //TODO : Implement the track service from the model
    Log.d("HomeScreen", "HomeScreen")
    Log.d("HomeScreen", "Il y a ${viewModel.trackService.tracks.count()} pistes")
    //TODO

    Scaffold(
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex.value,
                modifier = Modifier.fillMaxWidth()
            ) {
                HomeTabs.entries.forEachIndexed { index, currentTab ->
                    Tab(
                        selected = selectedTabIndex.value == index,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.outline,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(currentTab.ordinal)
                            }
                        },
                        text = { Text(text = currentTab.text) },
                        icon = {
                            Icon(
                                imageVector = if (selectedTabIndex.value == index) {
                                    ImageVector.vectorResource(id = currentTab.selectedIcon)
                                } else {
                                    ImageVector.vectorResource(id = currentTab.unselectedIcon)
                                },
                                contentDescription = null
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (HomeTabs.entries[pagerState.currentPage]) {
                        HomeTabs.Tracks -> {
                            TrackScreen(viewModel = viewModel, navController = navController)
                        }
                        HomeTabs.SkiLifts -> {
                            SkiLiftScreen(skiLifts = listOf(skiLift), navController = navController)
                            //TODO :Use real data for ski lifts
                        }
                    }
                }
            }
        }
    }
}

val skiLift = SkiLift(
    name = "Ski Lift 1",
    type = SkiLiftType.CHAIRLIFT,
    status = Status.OPEN,
    hop = emptyList(),
    comments = hashMapOf(),
    id = UUID.randomUUID()
)