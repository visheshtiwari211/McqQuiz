package com.example.mcqquiz.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mcqquiz.Converters
import com.example.mcqquiz.models.McqQuestionEntity

@Database(entities = [McqQuestionEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class McqDatabase: RoomDatabase() {
    abstract fun quizDao(): QuizDao
}