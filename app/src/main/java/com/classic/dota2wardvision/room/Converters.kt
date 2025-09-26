package com.classic.dota2wardvision.room

import androidx.room.TypeConverter
import com.classic.dota2wardvision.openDotAAPI.WardMapResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromWardMapResponse(value: WardMapResponse?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toWardMapResponse(value: String): WardMapResponse? {
        val type = object : TypeToken<WardMapResponse>() {}.type
        return gson.fromJson(value, type)
    }
}