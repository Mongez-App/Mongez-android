package com.iti.mongez.domain.organization.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.organization.model.TeamEvent
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import javax.inject.Inject

class GetTeamEventsUseCase @Inject constructor(
    private val repository: OrganizationRepository
) {
    suspend operator fun invoke(teamId: String): Result<List<TeamEvent>> {
        return repository.getTeamEvents(teamId)
    }
}
