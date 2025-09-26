package com.classic.dota2wardvision.room

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val dao: PlayerReportDao
) {
    suspend fun getPlayerReport(steamId: Long) = dao.getPlayerReport(steamId)
    suspend fun insertPlayerReport(report: PlayerReport) = dao.insert(report)

    val allReports: Flow<List<PlayerReport>> = dao.getAllReports()

}
