package com.snowaze.app.screens.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.snowaze.app.R
import com.snowaze.app.compose.home.track.IconChip
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status
import com.snowaze.app.model.Track
import com.snowaze.app.model.TrackService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.saket.telephoto.zoomable.ZoomableContentTransformation

import me.saket.telephoto.zoomable.coil.ZoomableAsyncImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import me.saket.telephoto.zoomable.rememberZoomableState
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: MapViewModel = hiltViewModel(),
    trackService: TrackService = viewModel.trackService
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetScaffoldState().bottomSheetState,
    )

    val coroutineScope = rememberCoroutineScope()

    val markers by trackService.markers.collectAsState(initial = emptyList())
    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContent(bottomSheetScaffoldState.bottomSheetState.currentValue, markers)
        },
        scaffoldState = bottomSheetScaffoldState,
        // ... other BottomSheetScaffold properties
    ) {
        ZoomableImageWithMarkers(
            imageUrl = "https://www.envie-de-queyras.com/upload/plan-pistes/2015-2016/plan-des-pistes-alpin-molines-saint-veran-domaine-beauregard.jpg",
            contentDescription = "Ski Map",
            markers = markers,
            bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
            onMarkerClick = { marker ->
                coroutineScope.launch {
                    markers.forEach { it.isSelected.value = false } // Reset other markers
                    marker.isSelected.value = true
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZoomableImageWithMarkers(
    imageUrl: String,
    contentDescription: String,
    markers: List<ImageMarker>,
    onMarkerClick: (ImageMarker) -> Unit,
    bottomSheetState: SheetState
) {
    val zoomableState = rememberZoomableImageState(rememberZoomableState())
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
    ) {
        ZoomableAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { newSize ->
                    canvasSize = Size(newSize.width.toFloat(), newSize.height.toFloat())
                }
                .padding(
                    bottom = 35.dp
                )
                .clickable { // Add click listener to the Box
                    markers.forEach { it.isSelected.value = false } // Reset all markers
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                },
            model = imageUrl,
            contentDescription = contentDescription,
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop,
            state = zoomableState

        )

        markers.forEach { marker ->
            Marker(
                marker = marker,
                canvasSize = canvasSize,
                zoom = zoomableState.zoomableState.contentTransformation.getMaxScale(),
                offset = zoomableState.zoomableState.contentTransformation.offset,
                onClick = { onMarkerClick(marker) }
            )
        }
    }
}

fun ZoomableContentTransformation.getMaxScale(): Float {
    return maxOf(scale.scaleX, scale.scaleY)
}

@Composable
fun Marker(
    marker: ImageMarker,
    canvasSize: Size,
    zoom: Float,
    offset: Offset,
    onClick: () -> Unit
) {
    var markerSize = 30.dp
    val markerSizePx = with(LocalDensity.current) { markerSize.toPx() }
    val position = Offset(
        x = (marker.x * zoom) + offset.x - (markerSizePx / 2),
        y = (marker.y * zoom) + offset.y - (markerSizePx / 2)
    )

    // Ensure marker stays within bounds
    if (position.x > (-markerSizePx) && position.y > (-markerSizePx) &&
        position.x < canvasSize.width + (markerSizePx) && position.y < canvasSize.height + (markerSizePx)
    ) {
        val isSelected = marker.isSelected.value
        var colorBorder: Color? = null
        if(marker.isSelected.value) {
            Log.d("Marker", "Selected : ${marker.data.value}")
            val color = marker.data.value.let { it ->
                when (it) {
                    is Track -> it.difficulty.let {
                        when (it) {
                            Difficulty.GREEN -> Color.Green
                            Difficulty.BLUE -> Color.Blue
                            Difficulty.RED -> Color.Red
                            Difficulty.BLACK -> Color.DarkGray
                        }
                    }
                    is SkiLift -> it.status.let {
                        when (it) {
                            Status.OPEN -> Color.Green
                            Status.CLOSED -> Color.Red
                            Status.UNKNOWN -> Color.DarkGray
                        }
                    }
                    else -> Color.Black
                }
            }

            colorBorder = color
            markerSize+= 3.dp
        }

        Box(
            modifier = Modifier.offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
                .size(markerSize)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black)
                .clickable { onClick() }.border(
                    width = 3.dp,
                    color = colorBorder ?: Color.Transparent,
                    shape = RoundedCornerShape(4.dp),
                ),
            contentAlignment = Alignment.Center
        ) {
            if(marker.data.value is Track){
                val track = marker.data.value as Track
                FaIcon(
                    faIcon = FaIcons.Skiing,
                    tint = track.status.value.let {
                        when (it) {
                            Status.OPEN -> Color.Green
                            Status.CLOSED -> Color.Red
                            else -> Color.Black
                        }
                    } ?: Color.Black,
                    modifier = Modifier.size(markerSize).padding(6.dp),
                )
            } else if(marker.data.value is SkiLift){
                val skiLift = marker.data.value as SkiLift
                Icon(
                    imageVector = when (skiLift.type) {
                        SkiLiftType.CHAIRLIFT -> ImageVector.vectorResource(id = R.drawable.chair_lift_icon)
                        SkiLiftType.GONDOLA -> ImageVector.vectorResource(id = R.drawable.gondola_lift_icon)
                        SkiLiftType.TBAR -> ImageVector.vectorResource(id = R.drawable.noun_ski_lift_8803__1_)
                    },
                    contentDescription = "Ski Lift Icon",
                    tint = skiLift.status.let {
                        when (it) {
                            Status.OPEN -> Color.Green
                            Status.CLOSED -> Color.Red
                            Status.UNKNOWN -> Color.Black
                        }
                    },
                    modifier = Modifier.size(64.dp).padding(6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(sheetValue: SheetValue, markers: List<ImageMarker>) {
    val currentMarker = markers.find { it.isSelected.value}
    if (currentMarker != null) {
        when (val data = currentMarker.data.value) {
            is Track -> TrackDetailContent(data)
            is SkiLift -> SkiLiftDetailContent(data)
            // ... add other content for different marker types
            else -> {} // Handle unknown marker types
        }
    }
}

@Composable
fun TrackDetailContent(track: Track) {
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
            Text(
                modifier = Modifier.padding(bottom = 15.dp),
                text = if (track.section != 0) {
                    "${track.name} - ${track.section}"
                } else {
                    track.name
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = track.status.value.toString(),
                color = when (track.status.value) {
                    Status.OPEN -> Color.Green
                    Status.CLOSED -> Color.Red
                    else -> {
                        Color.Black
                    }
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }
        IconChip(
            text = track.difficulty.value,
            difficulty = track.difficulty,
            modifier = Modifier
                .height(38.dp)
                .requiredWidth(90.dp)
        )
    }
}

@Composable
fun SkiLiftDetailContent(skiLift: SkiLift) {
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
        tint = Color.Black,
        modifier = Modifier.size(64.dp).padding(end = 32.dp)
    )
    Text(
        text = skiLift.name,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.align(Alignment.CenterVertically)
    )
}
        Text(
            text = skiLift.status.toString(),
            color = when (skiLift.status) {
                Status.OPEN -> Color.Green
                Status.CLOSED -> Color.Red
                Status.UNKNOWN -> Color.Black
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        }
    }
}