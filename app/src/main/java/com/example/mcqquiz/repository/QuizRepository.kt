package com.example.mcqquiz.repository

import com.example.mcqquiz.api.QuizApi
import com.example.mcqquiz.data.QuizDao
import com.example.mcqquiz.mapper.MapperFunctions.toDaoList
import com.example.mcqquiz.mapper.MapperFunctions.toUiModelList
import com.example.mcqquiz.models.McqLocalState
import com.example.mcqquiz.models.McqUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizApi: QuizApi,
    private val quizDao: QuizDao
) {

    suspend fun getAllMcq(): Flow<List<McqUIModel>> {
        val mcqPayloadDto = quizApi.getAllMcqPayload()
        if (mcqPayloadDto.isSuccessful) {
            println("${mcqPayloadDto.body()?.toDaoList()}")
            val mcqEntityList = mcqPayloadDto.body()?.toDaoList() ?: listOf()
            val localEntityList = quizDao.getAllLocalData()
            val localStateMap: Map<Int, McqLocalState> =
                localEntityList.associate { entity ->
                    entity.mcqId to McqLocalState(
                        isAnswered = entity.isAnswered,
                        isCorrectlyAnswered = entity.isCorrectlyAnswered,
                        isSkipped = entity.isSkipped
                    )
                }

            val mergedList = mcqEntityList.map {
                val local = localStateMap[it.mcqId]

                if (local != null) {
                    it.copy(
                        isAnswered = local.isAnswered,
                        isCorrectlyAnswered = local.isCorrectlyAnswered,
                        isSkipped = local.isSkipped
                    )
                } else {
                    it
                }
            }
            if (mcqEntityList.isNotEmpty()) {
                quizDao.insertAllMcq(mergedList)
            }
        }
        return quizDao.getAllMcq().map { it.toUiModelList() }
    }

    suspend fun updateIsAnswered(mcqId: Int, isAnswered: Boolean, isCorrect: Boolean, isSkipped: Boolean) {
        println("inside updateIsAnswered: mcqId: $mcqId -- isAnswered: $isAnswered")
        quizDao.updateIsAnswered(mcqId = mcqId, answered = isAnswered, isCorrect = isCorrect, isSkipped)
    }

    suspend fun resetQuiz() {
        quizDao.resetQuiz()
    }
}