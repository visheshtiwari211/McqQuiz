package com.example.mcqquiz.repository

import com.example.mcqquiz.api.QuizApi
import com.example.mcqquiz.data.QuizDao
import com.example.mcqquiz.mapper.MapperFunctions.toDaoList
import com.example.mcqquiz.mapper.MapperFunctions.toUiModelList
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
            val localEntityMap: Map<Int, Boolean> =
                localEntityList.associate { it.mcqId to it.isAnswered }

            val mergedList = mcqEntityList.map {
                val answered = localEntityMap[it.mcqId] ?: false
                it.copy(isAnswered = answered)
            }
            if (mcqEntityList.isNotEmpty()) {
                quizDao.insertAllMcq(mergedList)
            }
        }
        return quizDao.getAllMcq().map { it.toUiModelList() }
    }

    suspend fun updateIsAnswered(mcqId: Int, isAnswered: Boolean) {
        println("inside updateIsAnswered: mcqId: $mcqId -- isAnswered: $isAnswered")
        quizDao.updateIsAnswered(mcqId = mcqId, answered = isAnswered)
    }
}