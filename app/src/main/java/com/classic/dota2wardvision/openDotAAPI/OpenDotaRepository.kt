package com.classic.dota2wardvision.openDotAAPI

import android.util.Log
import java.net.SocketTimeoutException
import javax.inject.Inject

class OpenDotaRepository @Inject constructor(private val api: OpenDotaApi) {
    suspend fun searchPlayers(name: String): List<PlayerSearchResponse>? {
        return try {
            api.searchPlayers(name)
        }catch (e: SocketTimeoutException) {
            Log.e("SteamRepository", "Timeout error: ${e.message}")
            null // or return an empty response
        } catch (e: Exception) {
            Log.e("SteamRepository", "Other error: ${e.message}")
            null
        }
    }

    suspend fun getRecentMatches(accountId: Long): List<RecentMatchResponse> {
        return api.getRecentMatches(accountId)
    }

    suspend fun getWardMap(accountId: Long): WardMapResponse {
        return api.getWardMap(accountId)
    }
}