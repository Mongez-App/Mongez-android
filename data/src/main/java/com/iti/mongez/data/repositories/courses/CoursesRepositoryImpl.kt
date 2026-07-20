package com.iti.mongez.data.repositories.courses

import com.iti.mongez.data.dtos.CreateCourseRequestDto
import com.iti.mongez.domain.core.Result
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.CoursesApiService
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.courses.model.CourseActionResponse
import com.iti.mongez.domain.courses.model.CourseMaterial
import com.iti.mongez.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(
    private val apiService: CoursesApiService
) : CoursesRepository {

    override suspend fun getCourses(): Result<List<Course>> = safeApi {
        // Removed .body()!!
        val response = apiService.getCourses()
        response.map { it.toDomain() }
    }

    override suspend fun createCourse(
        name: String,
        courseCode: String,
        startDate: String,
        examDate: String,
        hasMaterials: Boolean
    ): Result<CourseCreationResult> = safeApi {
        val request = CreateCourseRequestDto(name, courseCode, startDate, examDate, hasMaterials)
        // Removed .body()!!
        val response = apiService.createCourse(request)
        response.toDomain()
    }

    override suspend fun getCourseDetails(courseId: String): Result<Course> = Result.Failure(NotImplementedError())
    override suspend fun updateCourse(courseId: String, name: String, isHidden: Boolean): Result<CourseActionResponse<Course>> = Result.Failure(NotImplementedError())
    override suspend fun deleteCourse(courseId: String): Result<CourseActionResponse<Unit>> = Result.Failure(NotImplementedError())
    
    override suspend fun getCourseMaterials(courseId: String): Result<List<CourseMaterial>> = Result.Failure(NotImplementedError())
    override suspend fun uploadCourseMaterial(courseId: String, fileName: String, contentType: String, fileSizeBytes: Long): Result<CourseActionResponse<String>> = Result.Failure(NotImplementedError())
    override suspend fun deleteCourseMaterial(courseId: String, materialId: String): Result<CourseActionResponse<Unit>> = Result.Failure(NotImplementedError())
}