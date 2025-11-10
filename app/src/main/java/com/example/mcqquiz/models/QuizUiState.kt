package com.example.mcqquiz.models

import androidx.compose.runtime.Immutable

@Immutable
data class QuizUiState(
    val questions: List<McqUIModel> = emptyList(),
    val currentIndex: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0
)
