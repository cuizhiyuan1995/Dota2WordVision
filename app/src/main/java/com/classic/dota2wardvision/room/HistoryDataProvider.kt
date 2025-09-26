package com.classic.dota2wardvision.room

import android.content.Context
import androidx.room.Room

object HistoryDataProvider {
    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return db ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "player_reports_db"
            ).build()
            db = instance
            instance
        }
    }
}