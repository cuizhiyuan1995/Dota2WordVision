package com.classic.dota2wardvision.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.classic.dota2wardvision.ui.navigation.Screen
import com.classic.dota2wardvision.viewModel.SearchViewModel
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val networkError = viewModel.networkerror

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter Player Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (query.isNotBlank()) {
                viewModel.searchPlayer(query) {
                    // Navigate to result screen with ID as argument
                    navController.navigate(Screen.SearchResult.route)
                }
            }
        },
            enabled = !viewModel.isLoading
        ) {
            Text("Search")
        }
    }

    // Overlay loading dialog
    if (viewModel.isLoading) {
        Dialog(onDismissRequest = { /* prevent dismiss */ }) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    if (networkError) {
        AlertDialog(
            onDismissRequest = { viewModel.setNetworkError(false) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setNetworkError(false)   // dismiss
                    viewModel.searchPlayer(query) {
                        // Navigate to result screen with ID as argument
                        navController.navigate(Screen.SearchResult.route)
                    }   // retry
                }) {
                    Text("Retry")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setNetworkError(false) }) {
                    Text("Cancel")
                }
            },
            title = { Text("Error") },
            text = { Text("Network timeout. Please retry.") }
        )
    }
}