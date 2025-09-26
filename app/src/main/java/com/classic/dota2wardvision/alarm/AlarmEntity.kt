package com.classic.dota2wardvision.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startMinute: Int,
    val startSecond: Int,
    val repeatMinute: Int,
    val repeatSecond: Int,
    val alarmText: String
)

