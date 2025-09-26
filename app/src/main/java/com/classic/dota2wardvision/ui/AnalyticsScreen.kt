package com.classic.dota2wardvision.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.classic.dota2wardvision.R
import com.classic.dota2wardvision.viewModel.SearchViewModel

@Composable
fun AnalyticsScreen(
    navController: NavHostController,
    viewModel: SearchViewModel
) {
    val playerID = viewModel.playerID

//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(playerID.toString())
//    }

    val wardMap by viewModel.analyticsData.collectAsState()
    val isLoading by viewModel.isReportLoading.collectAsState()

//    Column(modifier = Modifier.padding(16.dp)) {
//        Text("Ward Map Response:")
//
//        if (wardMap != null) {
//            // Pretty print as JSON string
//            Text(wardMap.toString())
//        } else {
//            Text("Loading ward map...")
//        }
//    }

//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // Background map
//        Image(
//            painter = painterResource(id = R.drawable.minimap),
//            contentDescription = "Ward map",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.Fit // or Crop, depends on map aspect ratio
//        )
//
//        // Draw dots for wards
//        wardMap?.obs?.forEach { (x, yMap) ->
//            yMap.forEach { (y,count) ->
//                WardDot(
//                    x = x.toInt(),
//                    y = y.toInt(),
//                    color = wardColor(count)
//                )
//            }
//        }
//    }

    BoxWithConstraints(
        modifier = Modifier.wrapContentSize()
    ) {
        val mapPainter = painterResource(id = R.drawable.gamemap_7_39_minimap_dota2_gameasset)

        val mapWidth = constraints.maxWidth
        val mapHeight = constraints.maxHeight
        Log.d("AnalyticsScreenWidthHeight",mapWidth.toString() + "," + mapHeight.toString())

        var imageSize by remember { mutableStateOf(IntSize.Zero) }
        Log.d("AnalyticsScreenImageSize",imageSize.toString())

        // Max values from API
        val maxCoord = 192f
        val minCoord = 64f

        // Draw map
        Image(
            painter = mapPainter,
            contentDescription = "Ward Map",
            //modifier = Modifier.wrapContentSize(),
            contentScale = ContentScale.Fit,
            modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                imageSize = layoutCoordinates.size
            }
        )



        // Draw dots for wards
        wardMap?.obs?.forEach { (x, yMap) ->
            yMap.forEach { (y,count) ->
                Log.d("AnalyticsXRatio",( (x.toInt() - minCoord) / ( maxCoord - minCoord) ).toString())
                Log.d("AnalyticsYRatio",( (maxCoord - y.toInt()) / ( maxCoord - minCoord) ).toString())
                WardDot(
                    x = ( (x.toInt() - minCoord) / ( maxCoord - minCoord) ) * imageSize.width.toFloat(),
                    y = ( (maxCoord - y.toInt()) / ( maxCoord - minCoord) ) * imageSize.height.toFloat(),
                    // api 0,0 is at bottom left corner
                    color = wardColor(count)
                )
            }
        }

    }


        // Loading dialog
    if (isLoading) {
        Dialog(onDismissRequest = { /* prevent dismiss */ }) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun WardDot(
    x: Float,
    y: Float,
    color: Color = Color.Red
) {
    val dotXDp = with(LocalDensity.current) { x.toDp() }
    val dotYDp = with(LocalDensity.current) { y.toDp() }
    //Log.d("AnalyticsXandY", dotXDp.toString() + "," + dotYDp.toString())
    Box(
        modifier = Modifier
            .offset(
                dotXDp,  // scale later
                dotYDp
            )
            .size(8.dp)
            .background(color, shape = CircleShape)
    )
}

fun wardColor(count: Int): Color {
    return when {
        count == 1 -> Color.Green
        count == 2 -> Color(0xFFFFA500) // Orange
        count >= 3 -> Color.Red
        else -> Color.Gray
    }
}

