package com.snowaze.app.compose.itinerary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.snowaze.app.R
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.IPath
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    navController: NavHostController,
    viewModel: ItineraryViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.Top
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                SearchBar1(viewModel = viewModel)
                Spacer(modifier = Modifier.height(16.dp))
                SearchBar2(viewModel = viewModel)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (viewModel.isBothLocked()) {
                item {
                    val path1 = viewModel.fullList.find { it.name == viewModel.searchQuery }
                    val path2 = viewModel.fullList.find { it.name == viewModel.searchQuery2 }
                    if (path1 != null && path2 != null) {
                        val fullPathList = viewModel.trackService.getPath(path1.id, path2.id)
                        if (fullPathList.isNotEmpty()) {
                            fullPathList.forEach {
                                Spacer(modifier = Modifier.height(16.dp))
                                PathCard(it)
                            }
                        }
                    } else {
                        Text(text = "Paths not found")
                    }
                }
            } else {
                item{
                    Text(
                        modifier = Modifier.padding(50.dp),
                        text = "Please select a path"
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PathCard(paths: List<IPath>) {

    //extract list of tracks in path
    val tracks = paths.filterIsInstance<Track>()
    val maxDifficulty = tracks.maxOf { it.difficulty }

    var isCardDeployed by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .combinedClickable(
                enabled = true,
                onClick = {
                    isCardDeployed = !isCardDeployed
                },
                onLongClick = {
                    /*TODO*/
                }
            )
            .fillMaxWidth()
            .heightIn(min = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(10.dp) // Ajoutez cette ligne pour arrondir les coins
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(40.dp)
                ) {
                    Text(
                        text = " ${paths.size - 2} hops - max difficulty :  ",
                    )
                    Image(
                        modifier = Modifier.height(20.dp),
                        painter = painterResource(id = R.drawable.dot_svgrepo_com),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            when (maxDifficulty) {
                                Difficulty.GREEN -> Color.Green
                                Difficulty.BLUE -> Color.Blue
                                Difficulty.RED -> Color.Red
                                Difficulty.BLACK -> Color.Black
                            }
                        )
                    )
                }
                if (isCardDeployed) {
                    paths.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                                .height(40.dp)

                        ) {
                            when (val path = it) {
                                is Track -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.dot_svgrepo_com),
                                        contentDescription = null,
                                        modifier = Modifier.padding(13.dp, 13.dp, 6.dp, 13.dp),
                                        colorFilter = ColorFilter.tint(
                                            when (path.difficulty) {
                                                Difficulty.GREEN -> Color.Green
                                                Difficulty.BLUE -> Color.Blue
                                                Difficulty.RED -> Color.Red
                                                Difficulty.BLACK -> Color.Black
                                            }
                                        )
                                    )
                                }

                                is SkiLift -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.skilift_icon),
                                        contentDescription = null,
                                        modifier = Modifier.padding(13.dp, 13.dp, 6.dp, 13.dp),
                                        colorFilter = ColorFilter.tint(Color.Gray)
                                    )
                                }
                            }
                            Text(
                                when (it) {
                                    is Track -> if (it.section != 0) "${it.name} - ${it.section}" else it.name
                                    is SkiLift -> it.name
                                    else -> ""
                                }
                            )
                        }
                    }
                }
            }
            IconButton(onClick = {
                isCardDeployed = !isCardDeployed
            }) { // Ajoutez ce bouton pour la flèche
                Icon(
                    imageVector = if (isCardDeployed) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun SearchBar1(viewModel: ItineraryViewModel) {

    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    SearchBar1(
        searchQuery = viewModel.searchQuery,
        searchResults = searchResults,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        isSearchActive = viewModel.isSearchActive,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar1(
    searchQuery: String,
    searchResults: List<IPath>,
    isSearchActive: Boolean,
    viewModel: ItineraryViewModel,
    onSearchQueryChange: (String) -> Unit
) {

    DockedSearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { /* Handle search */ },
        placeholder = { Text(text = "Departure") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty() && isSearchActive) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        content = {
            if (isSearchActive && searchResults.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    items(
                        count = searchResults.size,
                        key = { index -> searchResults[index].id },
                        itemContent = { index ->
                            val path = searchResults[index]
                            PathListItem1(path = path, viewModel = viewModel)
                        }
                    )
                }
            } else if (isSearchActive && searchResults.isEmpty()) {
                PathListEmptyState()
            }
        },
        active = isSearchActive,
        onActiveChange = { viewModel.onSearchActiveChange(it) },
        tonalElevation = 0.dp
    )
}


@Composable
fun PathListItem1(
    path: IPath,
    modifier: Modifier = Modifier,
    viewModel: ItineraryViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                viewModel.onSearchQueryChange(path.name)
                viewModel.onSearchActiveChange(false)
                viewModel.onLock1(true)
            }
    ) {
        when (path) {
            is Track -> {
                Text(text = if (path.section != 0) "${path.name} - ${path.section}" else path.name)
            }

            is SkiLift -> {
                Text(text = path.name)
            }
        }
    }
}

@Composable
fun PathListEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "No places found",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "Try adjusting your search",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun SearchBar2(viewModel: ItineraryViewModel) {

    val searchResults by viewModel.searchResults2.collectAsStateWithLifecycle()

    SearchBar2(
        searchQuery = viewModel.searchQuery2,
        searchResults = searchResults,
        onSearchQueryChange = { viewModel.onSearchQueryChange2(it) },
        isSearchActive = viewModel.isSearchActive2,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar2(
    searchQuery: String,
    searchResults: List<IPath>,
    isSearchActive: Boolean,
    viewModel: ItineraryViewModel,
    onSearchQueryChange: (String) -> Unit
) {

    DockedSearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { /* Handle search */ },
        placeholder = { Text(text = "Destination") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty() && isSearchActive) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "Clear search"
                    )
                }
            }
        },
        content = {
            if (isSearchActive && searchResults.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = searchResults.size,
                        key = { index -> searchResults[index].id },
                        itemContent = { index ->
                            val path = searchResults[index]
                            PathListItem2(path = path, viewModel = viewModel)
                        }
                    )
                }
            } else if (isSearchActive && searchResults.isEmpty()) {
                PathListEmptyState()
            }
        },
        active = isSearchActive,
        onActiveChange = { viewModel.onSearchActiveChange2(it) },
        tonalElevation = 0.dp
    )
}


@Composable
fun PathListItem2(
    path: IPath,
    modifier: Modifier = Modifier,
    viewModel: ItineraryViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                viewModel.onSearchQueryChange2(path.name)
                viewModel.onSearchActiveChange2(false)
                viewModel.onLock2(true)
            }
    ) {
        when (path) {
            is Track -> {
                Text(text = if (path.section != 0) "${path.name} - ${path.section}" else path.name)
            }

            is SkiLift -> {
                Text(text = path.name)
            }
        }
    }
}