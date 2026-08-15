package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto
import com.iti.mongez.data.dtos.organizationdtos.MyTeamsResponseDto

interface OrganizationRemoteDataSource {
    suspend fun getTeams(): MyTeamsResponseDto
    suspend fun getDiscoverTeams(): DiscoverTeamsResponseDto
}
