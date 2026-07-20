package com.iti.mongez.domain.courses.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class GetCourseDetailsUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String): Result<Course> {
        return coursesRepository.getCourseDetails(courseId)
    }
}
