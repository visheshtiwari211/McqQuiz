package com.example.mcqquiz.mapper

import com.example.mcqquiz.models.AllMcqPayloadDto
import com.example.mcqquiz.models.AllMcqPayloadItemDto
import com.example.mcqquiz.data.McqQuestionEntity
import com.example.mcqquiz.models.McqUIModel

object MapperFunctions {
    fun AllMcqPayloadItemDto.toDao(): McqQuestionEntity {
        return McqQuestionEntity(
            mcqId = this.id,
            correctOptionIndex = this.correctOptionIndex,
            options = this.options,
            question = this.question,
            isAnswered = false,
            isCorrectlyAnswered = false,
            isSkipped = false
        )
    }
    fun AllMcqPayloadDto.toDaoList(): List<McqQuestionEntity> {
        return this.map { it.toDao() }
    }

    fun List<McqQuestionEntity>.toUiModelList(): List<McqUIModel> {
        return this.map { it.toUiModel() }
    }

    fun McqQuestionEntity.toUiModel(): McqUIModel {
        return McqUIModel(
            mcqId = this.mcqId,
            correctOptionIndex = this.correctOptionIndex,
            options = this.options,
            question = this.question,
            isAnswered = this.isAnswered,
            isCorrectlyAnswered = this.isCorrectlyAnswered,
            isSkipped = this.isSkipped
        )
    }
}