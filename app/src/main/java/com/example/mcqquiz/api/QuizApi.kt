package com.example.mcqquiz.api

import com.example.mcqquiz.models.AllMcqPayloadDto
import retrofit2.Response
import retrofit2.http.GET

interface QuizApi {
    @GET("dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw")
    suspend fun getAllMcqPayload(): Response<AllMcqPayloadDto>
}