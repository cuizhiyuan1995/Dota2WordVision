package com.classic.dota2wardvision.room

import androidx.room.PrimaryKey
import androidx.room.Entity
import com.classic.dota2wardvision.openDotAAPI.WardMapResponse

@Entity(tableName = "player_reports")
data class PlayerReport(
    @PrimaryKey val steamId: Long,
    val profileName: String,
    val profilePicUrl: String,
    val wardMapJson: WardMapResponse // you can store raw JSON or serialize with TypeConverters
)

