package com.classic.dota2wardvision.ui.navigation

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Analytics : Screen("analytics")
    object History : Screen("history")
    object SearchResult : Screen("searchresult")
    object HistoryResult : Screen("historyresult")
    object Alarm: Screen("alarm")
    object Setting: Screen("setting")
    object VersionHistory: Screen("versionhistory")
}