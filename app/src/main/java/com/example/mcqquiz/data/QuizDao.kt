package com.example.mcqquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMcq(mcqList: List<McqQuestionEntity>)

    @Query("SELECT * FROM mcq")
    fun getAllMcq(): Flow<List<McqQuestionEntity>>

    @Query("UPDATE mcq SET isAnswered = :answered, isCorrectlyAnswered = :isCorrect,isSkipped = :isSkipped WHERE mcqId = :mcqId")
    suspend fun updateIsAnswered(mcqId: Int, answered: Boolean, isCorrect: Boolean, isSkipped: Boolean)

    @Query("SELECT * FROM mcq")
    suspend fun getAllLocalData(): List<McqQuestionEntity>

    @Query("UPDATE mcq SET isAnswered = 0, isCorrectlyAnswered = 0,isSkipped = 0")
    suspend fun resetQuiz()
}