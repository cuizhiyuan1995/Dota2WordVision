package com.classic.dota2wardvision.alarm

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AlarmRepository @Inject constructor(
    private val dao: AlarmDao
) {
    fun getAllAlarms(): Flow<List<AlarmEntity>> = dao.getAllAlarms()
    suspend fun insertAlarm(alarm: AlarmEntity) = dao.insertAlarm(alarm)
    suspend fun deleteAlarm(alarm: AlarmEntity) = dao.deleteAlarm(alarm)
}
