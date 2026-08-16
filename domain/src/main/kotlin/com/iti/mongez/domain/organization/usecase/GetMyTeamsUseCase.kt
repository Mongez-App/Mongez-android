package com.iti.mongez.domain.organization.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.organization.model.Team
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import javax.inject.Inject

class GetMyTeamsUseCase @Inject constructor(
    private val repository: OrganizationRepository
) {
    suspend operator fun invoke(): Result<List<Team>> {
        return repository.getMyTeams()
    }
}
