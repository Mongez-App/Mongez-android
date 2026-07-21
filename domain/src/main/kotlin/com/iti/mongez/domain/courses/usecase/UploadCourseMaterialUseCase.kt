package com.iti.mongez.domain.courses.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.CourseActionResponse
import com.iti.mongez.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class UploadCourseMaterialUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String, fileName: String, contentType: String, fileSizeBytes: Long): Result<CourseActionResponse<String>> {
        return coursesRepository.uploadCourseMaterial(courseId, fileName, contentType, fileSizeBytes)
    }
}
