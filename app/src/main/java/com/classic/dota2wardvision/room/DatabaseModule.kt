package com.classic.dota2wardvision.room

import android.app.Application
import androidx.room.Room
import com.classic.dota2wardvision.alarm.AlarmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "player_db")
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun providePlayerDao(db: AppDatabase): PlayerReportDao = db.playerReportDao()

    @Provides
    fun provideAlarmDao(db: AppDatabase): AlarmDao = db.alarmDao()
}
