package com.example.mcqquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mcqquiz.models.McqQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMcq(mcqList: List<McqQuestionEntity>)

    @Query("SELECT * FROM mcq")
    fun getAllMcq(): Flow<List<McqQuestionEntity>>

    @Query("UPDATE mcq SET isAnswered = :answered WHERE mcqId = :mcqId")
    suspend fun updateIsAnswered(mcqId: Int, answered: Boolean)
}