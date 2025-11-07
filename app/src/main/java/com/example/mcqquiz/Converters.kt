package com.example.mcqquiz

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromOptionsList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toOptionsList(json: String): List<String> {
        return Json.decodeFromString(json)
    }
}