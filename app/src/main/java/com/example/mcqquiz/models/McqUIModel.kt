package com.example.mcqquiz.models

import androidx.compose.runtime.Immutable

@Immutable
data class McqUIModel(
    val mcqId: Int,
    val correctOptionIndex: Int,
    val options: List<String>,
    val question: String,
    val isAnswered: Boolean
)
