package com.example.mcqquiz.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mcq")
data class McqQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo("mcqId")
    val mcqId: Int,
    @ColumnInfo(name = "correctOptionsIndex")
    val correctOptionIndex: Int,
    @ColumnInfo(name = "options")
    val options: List<String>,
    @ColumnInfo(name = "question")
    val question: String,
    @ColumnInfo(name = "isAnswered")
     val isAnswered: Boolean
)
