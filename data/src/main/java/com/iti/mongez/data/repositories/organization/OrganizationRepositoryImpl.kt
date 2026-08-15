package com.iti.mongez.data.repositories.organization

import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.sources.remote.interfaces.OrganizationRemoteDataSource
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.model.Team
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import javax.inject.Inject

class OrganizationRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrganizationRemoteDataSource
) : OrganizationRepository {

    override suspend fun getMyTeams(): Result<List<Team>> {
        return safeApi {
            val response = remoteDataSource.getTeams()
            response.teams.map { it.toDomain() }
        }
    }

    override suspend fun getDiscoverTeams(): Result<DiscoverTeams> {
        return safeApi {
            val response = remoteDataSource.getDiscoverTeams()
            response.toDomain()
        }
    }
}
