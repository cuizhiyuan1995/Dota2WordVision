package com.classic.dota2wardvision.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.classic.dota2wardvision.R
import com.classic.dota2wardvision.viewModel.HistoryViewModel
import com.classic.dota2wardvision.viewModel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryResultScreen(
    navController: NavHostController,
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val selectedReport by viewModel.selectedReport.collectAsState()



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("History Result") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {} // leave empty to hide
    ) { innerPadding ->
        selectedReport?.let { report ->
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(innerPadding)
            ) {
                val mapPainter = painterResource(id = R.drawable.gamemap_7_39_minimap_dota2_gameasset)

//                val mapWidth = constraints.maxWidth
//                val mapHeight = constraints.maxHeight
//                Log.d("HistoryResultScreenWidthHeight",mapWidth.toString() + "," + mapHeight.toString())

                var imageSize by remember { mutableStateOf(IntSize.Zero) }
                Log.d("HistoryResultScreenImageSize",imageSize.toString())

                // Max values from API
                val maxCoord = 192f
                val minCoord = 64f

                // Draw map
                Image(
                    painter = mapPainter,
                    contentDescription = "Ward Map",
                    //modifier = Modifier.wrapContentSize(),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { layoutCoordinates ->
                        imageSize = layoutCoordinates.size
                    }
                )



                // Draw dots for wards
                report.obs?.forEach { (x, yMap) ->
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
        }
    }

}


