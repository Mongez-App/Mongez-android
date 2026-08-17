package com.iti.mongez.domain.organization.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import javax.inject.Inject

class GetTeamCoursesUseCase @Inject constructor(
    private val repository: OrganizationRepository
) {
    suspend operator fun invoke(teamId: String): Result<List<Course>> {
        return repository.getTeamCourses(teamId)
    }
}
