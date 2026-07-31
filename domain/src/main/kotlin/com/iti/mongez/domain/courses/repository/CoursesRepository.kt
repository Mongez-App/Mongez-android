package com.iti.mongez.domain.courses.repository

import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.CourseActionResponse
import com.iti.mongez.domain.courses.model.CourseMaterial

interface CoursesRepository {
    suspend fun getCourses(): Result<List<Course>>
    suspend fun createCourse(
        name: String,
        courseCode: String,
        imageUrl: String,
        startDate: String,
        examDate: String,
        courseType: String,
        materialUrl: String?
    ): Result<CourseCreationResult>
    suspend fun getCourseDetails(courseId: String): Result<Course>
    suspend fun updateCourse(courseId: String, name: String, isHidden: Boolean): Result<CourseActionResponse<Course>>
    suspend fun deleteCourse(courseId: String): Result<CourseActionResponse<Unit>>
    
    suspend fun getCourseMaterials(courseId: String): Result<List<CourseMaterial>>
    suspend fun uploadCourseMaterial(
        courseId: String,
        fileName: String,
        contentType: String,
        fileSizeBytes: Long,
        pageCount: Int,
        fileBytes: ByteArray // We now pass the physical file to the repository
    ): Result<CourseActionResponse<Unit>>    suspend fun deleteCourseMaterial(courseId: String, materialId: String): Result<CourseActionResponse<Unit>>
}