package com.classic.dota2wardvision.openDotAAPI

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaApi {
    // Search players by persona name (returns a list)
    @GET("search")
    suspend fun searchPlayers(
        @Query("q") query: String
    ): List<PlayerSearchResponse>

    // Get recent matches for a player
    @GET("players/{account_id}/recentMatches")
    suspend fun getRecentMatches(
        @Path("account_id") accountId: Long
    ): List<RecentMatchResponse>

    // Get ward map data for a player
    @GET("players/{account_id}/wardmap")
    suspend fun getWardMap(
        @Path("account_id") accountId: Long,
        //@Query("limit") limit: Int = 100,
        @Query("patch") limit: Int = 58
    ): WardMapResponse
}