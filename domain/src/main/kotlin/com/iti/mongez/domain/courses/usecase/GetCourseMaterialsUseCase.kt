package com.iti.mongez.domain.courses.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.CourseMaterial
import com.iti.mongez.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class GetCourseMaterialsUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String): Result<List<CourseMaterial>> {
        return coursesRepository.getCourseMaterials(courseId)
    }
}
