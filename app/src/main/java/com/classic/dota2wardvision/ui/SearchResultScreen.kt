package com.classic.dota2wardvision.ui

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.classic.dota2wardvision.openDotAAPI.PlayerSearchResponse
import com.classic.dota2wardvision.ui.navigation.Screen
import com.classic.dota2wardvision.viewModel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultScreen(
    navController: NavHostController,
    viewModel: SearchViewModel,
    onPlayerClick: (Long) -> Unit
) {
    val results = viewModel.searchResults // mutableStateOf from ViewModel
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlayer by remember { mutableStateOf<PlayerSearchResponse?>(null) }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Search Result") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {} // leave empty to hide
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(results.take(50)) { player ->
                PlayerItem(
                    name = player.personaname ?: "Unknown",
                    avatarUrl = player.avatarfull,
                    onClick = {
                        onPlayerClick(player.account_id)
                        showDialog = true
                        //selectedPlayer = player.account_id
                        selectedPlayer = player
                    }
                )
            }
        }
    }

    //confirmation dialog
    if (showDialog && selectedPlayer != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Generate Report") },
            text = { Text("Do you want to generate report for ${selectedPlayer!!.account_id}?") },
            confirmButton = {
                TextButton(onClick = {
                    // Parse data and navigate to analytics
                    viewModel.generateReport(selectedPlayer!!)

                    navController.navigate(Screen.Analytics.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        } // removes SearchResult from back stack
                        launchSingleTop = true
                        showDialog = false
                        Log.d("SearchResultScreen","searchscreen pop up")
                    }


                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("No")
                }
            }
        )
    }

}

@Composable
fun PlayerItem(name: String, avatarUrl: String?, onClick: () -> Unit) {
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
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

