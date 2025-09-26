package com.classic.dota2wardvision.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.classic.dota2wardvision.ui.navigation.Screen
import com.classic.dota2wardvision.viewModel.HistoryViewModel

@Composable
fun HistoryScreen(
    navController: NavHostController,
    viewModel: HistoryViewModel = hiltViewModel()
) {



//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text("History Screen")
//    }

    // Launch fetching when the composable enters composition
    val reports by viewModel.reports.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(reports.take(50)) { player ->
            ReportItem(
                name = player.profileName ?: "Unknown",
                avatarUrl = player.profilePicUrl,
                onClick = {
                    viewModel.selectReport(player.wardMapJson)  // Store selected report in ViewModel
                    navController.navigate(Screen.HistoryResult.route)
                },
                id = player.steamId
            )
        }
    }
}


@Composable
fun ReportItem(
    name: String,
    avatarUrl: String?,
    onClick: () -> Unit,
    id: Long
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = id.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}