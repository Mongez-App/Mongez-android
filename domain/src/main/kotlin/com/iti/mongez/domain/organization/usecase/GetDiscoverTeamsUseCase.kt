package com.iti.mongez.domain.organization.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import javax.inject.Inject

class GetDiscoverTeamsUseCase @Inject constructor(
    private val repository: OrganizationRepository
) {
    suspend operator fun invoke(): Result<DiscoverTeams> {
        return repository.getDiscoverTeams()
    }
}
