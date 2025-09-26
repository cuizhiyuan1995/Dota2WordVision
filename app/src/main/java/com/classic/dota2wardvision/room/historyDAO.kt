package com.classic.dota2wardvision.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playerReport: PlayerReport)

    @Query("SELECT * FROM player_reports WHERE steamId = :steamId LIMIT 1")
    suspend fun getPlayerReport(steamId: Long): PlayerReport?

    @Query("SELECT * FROM player_reports")
    fun getAllReports(): Flow<List<PlayerReport>>

    @Query("DELETE FROM player_reports")
    suspend fun clearAll()
}
