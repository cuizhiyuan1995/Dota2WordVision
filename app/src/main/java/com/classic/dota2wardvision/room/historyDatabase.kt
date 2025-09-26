package com.classic.dota2wardvision.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.classic.dota2wardvision.alarm.AlarmDao
import com.classic.dota2wardvision.alarm.AlarmEntity

@Database(entities = [PlayerReport::class,AlarmEntity::class], version = 2,exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerReportDao(): PlayerReportDao
    abstract fun alarmDao(): AlarmDao
}
