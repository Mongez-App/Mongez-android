package com.iti.mongez.data.repositories.courses

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.courses.model.CourseActionResponse
import com.iti.mongez.domain.courses.model.CourseMaterial
import com.iti.mongez.domain.courses.repository.CoursesRepository
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class FakeCoursesRepositoryImpl @Inject constructor() : CoursesRepository {

    // In-memory database to hold our dummy data
    private val fakeCourses = mutableListOf(
        Course(
            id = UUID.randomUUID().toString(),
            name = "Android Native Development",
            courseCode = "AND-101",
            startDate = "2026-07-20T00:00:00Z",
            examDate = "2026-09-15T00:00:00Z",
            hasMaterials = true,
            completionPercentage = 65f,
            isHidden = false
        ),
        Course(
            id = UUID.randomUUID().toString(),
            name = "Clean Architecture Patterns",
            courseCode = "ARC-404",
            startDate = "2026-08-01T00:00:00Z",
            examDate = "2026-10-01T00:00:00Z",
            hasMaterials = false,
            completionPercentage = 15f,
            isHidden = false
        )
    )

    override suspend fun getCourses(): Result<List<Course>> {
        // Simulate a 1-second network delay so you can see your loading spinner
        delay(1000.milliseconds)

        // Return a fresh list copy so StateFlow recognizes the state change
        return Result.Success(fakeCourses.toList())
    }

    override suspend fun createCourse(
        name: String,
        courseCode: String,
        startDate: String,
        examDate: String,
        hasMaterials: Boolean
    ): Result<CourseCreationResult> {
        // Simulate network delay for creating a course
        delay(1500.milliseconds)

        // Create the new domain model
        val newCourse = Course(
            id = UUID.randomUUID().toString(),
            name = name,
            courseCode = courseCode,
            startDate = startDate,
            examDate = examDate,
            hasMaterials = hasMaterials,
            completionPercentage = 0f, // New courses start at 0%
            isHidden = false
        )

        // Add it to our in-memory list
        fakeCourses.add(newCourse)

        // Return the success result
        val creationResult = CourseCreationResult(
            course = newCourse,
            alertMessage = "Course '$name' was successfully created!"
        )

        return Result.Success(creationResult)
    }

    override suspend fun getCourseDetails(courseId: String): Result<Course> = Result.Failure(NotImplementedError())
    override suspend fun updateCourse(courseId: String, name: String, isHidden: Boolean): Result<CourseActionResponse<Course>> = Result.Failure(NotImplementedError())
    override suspend fun deleteCourse(courseId: String): Result<CourseActionResponse<Unit>> = Result.Failure(NotImplementedError())
    
    override suspend fun getCourseMaterials(courseId: String): Result<List<CourseMaterial>> = Result.Failure(NotImplementedError())
    override suspend fun uploadCourseMaterial(courseId: String, fileName: String, contentType: String, fileSizeBytes: Long): Result<CourseActionResponse<String>> = Result.Failure(NotImplementedError())
    override suspend fun deleteCourseMaterial(courseId: String, materialId: String): Result<CourseActionResponse<Unit>> = Result.Failure(NotImplementedError())
}