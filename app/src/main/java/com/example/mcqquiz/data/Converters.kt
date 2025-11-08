package com.example.mcqquiz.data

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromOptionsList(list: List<String>): String {
        return Json.Default.encodeToString(list)
    }

    @TypeConverter
    fun toOptionsList(json: String): List<String> {
        return Json.Default.decodeFromString(json)
    }
}