package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.organizationdtos.MyTeamsResponseDto
import retrofit2.http.GET

interface OrganizationApiService {
    @GET("organization/getTeams")
    suspend fun getTeams(): MyTeamsResponseDto
}
