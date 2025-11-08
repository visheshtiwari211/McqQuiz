package com.example.mcqquiz.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.mcqquiz.data.Converters
import com.example.mcqquiz.data.McqQuestionEntity

@Database(entities = [McqQuestionEntity::class], version = 5, exportSchema = true)
@TypeConverters(Converters::class)
abstract class McqDatabase: RoomDatabase() {
    abstract fun quizDao(): QuizDao
}