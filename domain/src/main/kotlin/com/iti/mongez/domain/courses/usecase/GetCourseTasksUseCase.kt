package com.iti.mongez.domain.courses.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.repository.CoursesRepository
import com.iti.mongez.domain.courses.model.CourseTask
import javax.inject.Inject

class GetCourseTasksUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String): Result<List<CourseTask>> {
        return repository.getCourseTasks(courseId)
    }
}
