package com.iti.mongez.domain.courses.repository

import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.core.Result

interface CoursesRepository {
    suspend fun getCourses(): Result<List<Course>>
    suspend fun createCourse(
        name: String,
        courseCode: String,
        startDate: String,
        examDate: String,
        hasMaterials: Boolean
    ): Result<CourseCreationResult>
}