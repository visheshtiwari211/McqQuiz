package com.example.mcqquiz.repository

import com.example.mcqquiz.api.QuizApi
import com.example.mcqquiz.data.QuizDao
import com.example.mcqquiz.mapper.MapperFunctions.toDaoList
import com.example.mcqquiz.mapper.MapperFunctions.toUiModelList
import com.example.mcqquiz.models.McqUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizRepository @Inject constructor(private val quizApi: QuizApi, private val quizDao: QuizDao) {
    val getAllMcqFlow: Flow<List<McqUIModel>> = quizDao.getAllMcq().map { it.toUiModelList() }

    suspend fun getAllMcq() {
        val mcqPayloadDto = quizApi.getAllMcqPayload()
        if (mcqPayloadDto.isSuccessful) {
            val mcqEntityList = mcqPayloadDto.body()?.toDaoList() ?: listOf()
            if (mcqEntityList.isNotEmpty()) {
                quizDao.insertAllMcq(mcqEntityList)
            }
        }
    }

    suspend fun updateIsAnswered(mcqId: Int, isAnswered: Boolean) {
        quizDao.updateIsAnswered(mcqId = mcqId, answered = isAnswered)
    }
}