package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.sessiondtos.EndSessionRequestDto
import com.iti.mongez.data.dtos.sessiondtos.EndSessionResponseDto
import com.iti.mongez.data.dtos.sessiondtos.StartSessionRequestDto
import com.iti.mongez.data.dtos.sessiondtos.StartSessionResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface StudySessionApiService {
    @POST("sessions/start")
    suspend fun startSession(
        @Body request: StartSessionRequestDto
    ): StartSessionResponseDto

    @POST("sessions/{session_id}/end")
    suspend fun endSession(
        @Path("session_id") sessionId: String,
        @Body request: EndSessionRequestDto
    ): EndSessionResponseDto
}
