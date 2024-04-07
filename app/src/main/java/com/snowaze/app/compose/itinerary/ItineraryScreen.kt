package com.snowaze.app.compose.itinerary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.snowaze.app.model.IPath

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    navController: NavHostController,
    viewModel: ItineraryViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    SearchScreen(viewModel = viewModel)
                }
        }
    }

}

@Composable
fun SearchScreen(viewModel: ItineraryViewModel) {

    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    SearchScreen(
        searchQuery = viewModel.searchQuery,
        searchResults = searchResults,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
        isSearchActive = viewModel.isSearchActive,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<IPath>,
    isSearchActive : Boolean,
    viewModel: ItineraryViewModel,
    onSearchQueryChange: (String) -> Unit
) {

    DockedSearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        onSearch = { /* Handle search */ },
        placeholder = { Text(text = "Search") },
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
                            PathListItem(path = path, viewModel = viewModel)
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
fun PathListItem(
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
            }
    ) {
        Text(text = path.name)
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

