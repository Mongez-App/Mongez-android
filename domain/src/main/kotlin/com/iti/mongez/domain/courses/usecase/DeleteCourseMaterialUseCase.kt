package com.iti.mongez.domain.courses.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.CourseActionResponse
import com.iti.mongez.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class DeleteCourseMaterialUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String, materialId: String): Result<CourseActionResponse<Unit>> {
        return coursesRepository.deleteCourseMaterial(courseId, materialId)
    }
}
