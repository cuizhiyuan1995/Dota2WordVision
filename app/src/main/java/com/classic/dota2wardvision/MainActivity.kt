package com.classic.dota2wardvision

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.classic.dota2wardvision.ui.AlarmScreen
import com.classic.dota2wardvision.ui.AnalyticsScreen
import com.classic.dota2wardvision.ui.HistoryResultScreen
import com.classic.dota2wardvision.ui.HistoryScreen
import com.classic.dota2wardvision.ui.SearchResultScreen
import com.classic.dota2wardvision.ui.SearchScreen
import com.classic.dota2wardvision.ui.SettingsScreen
import com.classic.dota2wardvision.ui.VersionHistoryScreen
import com.classic.dota2wardvision.ui.navigation.Screen
import com.classic.dota2wardvision.ui.theme.Dota2WardVisionTheme
import com.classic.dota2wardvision.viewModel.AlarmViewModel
import com.classic.dota2wardvision.viewModel.HistoryViewModel
import com.classic.dota2wardvision.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dota2WardVisionTheme {
                 MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    searchViewModel: SearchViewModel = hiltViewModel(), // single instance
    historyViewModel: HistoryViewModel = hiltViewModel(),
    alarmViewModel : AlarmViewModel = hiltViewModel()
){
    val navController = rememberNavController() // simple navigation state
    val bottomScreens = listOf(
        Screen.Search,
        Screen.Analytics,
        Screen.History,
        Screen.Alarm
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route



    // --- Track back stack here ---
    val navStack = remember { mutableStateListOf<String>() }
    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Remove any destinations above the current one that were popped
            val route = destination.route ?: "unknown"
            val index = navStack.indexOf(route)
            if (index != -1) {
                navStack.subList(index + 1, navStack.size).clear()
            }
            // Add the current destination if it’s new
            if (navStack.lastOrNull() != route) {
                navStack.add(route)
            }
            Log.d("NavBackStack", "Stack now: $navStack")
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dota2 Ward Vision") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Setting.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            val noBottomBarScreens = setOf(
                Screen.Setting.route,
                Screen.VersionHistory.route,
            )
            if(currentRoute !in noBottomBarScreens){
                NavigationBar {
                    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
                    bottomScreens.forEach { screen ->
                        NavigationBarItem(
                            selected = currentDestination?.route == screen.route,
                            onClick = {
                                Log.d("Mainactivity_bottomClicked",screen.route)
                                navController.navigate(screen.route) {
                                    // pop up to root to avoid back stack piling
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                when (screen) {
                                    is Screen.Search -> Icon(Icons.Filled.Search, "Search")
                                    is Screen.Analytics -> Icon(
                                        painter = painterResource(id = R.drawable.baseline_analytics_black_18),
                                        contentDescription = "Analytics"
                                    )
                                    is Screen.History -> Icon(
                                        painter = painterResource(id = R.drawable.baseline_history_black_18),
                                        contentDescription = "History"
                                    )
                                    is Screen.Alarm -> Icon(Icons.Filled.Notifications, "Alarm")
                                    else -> {}
                                }
                            },
                            label = { Text(screen.route.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Search.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Search.route) { SearchScreen(navController,searchViewModel) }
            composable(Screen.Analytics.route) { AnalyticsScreen(navController,searchViewModel) }
            composable(Screen.History.route) { HistoryScreen(navController,historyViewModel) }
            composable(Screen.SearchResult.route) { SearchResultScreen(navController,searchViewModel,onPlayerClick = { }) }
            composable(Screen.HistoryResult.route) { HistoryResultScreen(navController,historyViewModel) }
            composable(Screen.Alarm.route) { AlarmScreen(navController,alarmViewModel) }
            composable(Screen.Setting.route) { SettingsScreen(navController) }
            composable(Screen.VersionHistory.route) { VersionHistoryScreen(navController) }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Dota2WardVisionTheme {
        MainScreen()
    }
}