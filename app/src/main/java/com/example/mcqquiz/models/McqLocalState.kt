package com.example.mcqquiz.models

data class McqLocalState(
    val isAnswered: Boolean,
    val isCorrectlyAnswered: Boolean,
    val isSkipped: Boolean
)
