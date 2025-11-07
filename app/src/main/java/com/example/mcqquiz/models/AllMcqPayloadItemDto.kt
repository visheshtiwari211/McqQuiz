package com.example.mcqquiz.models

data class AllMcqPayloadItemDto(
    val correctOptionIndex: Int,
    val id: Int,
    val options: List<String>,
    val question: String
)