package com.example.mcqquiz.models

data class QuizUiState(
    val questions: List<McqUIModel> = emptyList(),
    val currentIndex: Int = 0
)
