package com.iti.mongez.domain.courses.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(
        name: String,
        courseCode: String,
        imageUrl: String,
        startDate: String,
        examDate: String,
        courseType: String,
        materialUrl: String?
    ): Result<CourseCreationResult> {
        return repository.createCourse(
            name = name,
            courseCode = courseCode,
            imageUrl = imageUrl,
            startDate = startDate,
            examDate = examDate,
            courseType = courseType,
            materialUrl = materialUrl
        )
    }
}