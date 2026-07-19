package com.iti.mongez.domain.courses.usecase

import com.iti.mongez.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(
        name: String,
        courseCode: String,
        startDate: String,
        examDate: String,
        hasMaterials: Boolean
    ) = repository.createCourse(name, courseCode, startDate, examDate, hasMaterials)
}