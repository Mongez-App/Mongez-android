package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.coursesdtos.*
import okhttp3.MultipartBody

interface CoursesRemoteDataSource {
    suspend fun getCourses(): List<CourseDto>
    suspend fun getCourseDetails(courseId: String): CourseDto
    suspend fun createCourse(request: CreateCourseRequestDto): CourseCreationResponseDto
    suspend fun updateCourse(courseId: String, request: UpdateCourseRequestDto): CourseDto
    suspend fun deleteCourse(courseId: String): ActionStatusResponseDto
    suspend fun getCourseMaterials(courseId: String): List<CourseMaterialDto>
    suspend fun createMaterialMetadata(courseId: String, request: MaterialUploadRequestDto): MaterialUploadResponseDto
    suspend fun uploadMaterialFile(materialId: String, file: MultipartBody.Part): FileUploadResponseDto
    suspend fun deleteMaterial(courseId: String, materialId: String): ActionStatusResponseDto
    suspend fun addCourseEvent(courseId: String, request: AddEventRequestDto): AddEventResponseDto
    suspend fun getCourseTasks(courseId: String): CourseTasksResponseDto
}
