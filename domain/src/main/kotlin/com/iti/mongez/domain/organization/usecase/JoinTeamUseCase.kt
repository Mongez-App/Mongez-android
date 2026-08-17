package com.iti.mongez.domain.organization.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.organization.model.JoinTeamResponse
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import javax.inject.Inject

class JoinTeamUseCase @Inject constructor(
    private val repository: OrganizationRepository
) {
    suspend operator fun invoke(inviteCode: String): Result<JoinTeamResponse> {
        return repository.joinTeam(inviteCode)
    }
}
