package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.coursesdtos.*
import com.iti.mongez.data.sources.remote.interfaces.CoursesRemoteDataSource
import com.iti.mongez.data.sources.remote.services.CoursesApiService
import com.iti.mongez.data.sources.remote.services.TasksApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class CoursesRemoteDataSourceImpl @Inject constructor(
    private val coursesApiService: CoursesApiService,
    private val tasksApiService: TasksApiService
) : CoursesRemoteDataSource {
    
    override suspend fun getCourses(): List<CourseDto> {
        return coursesApiService.getCourses()
    }

    override suspend fun getCourseDetails(courseId: String): CourseDto {
        return coursesApiService.getCourseDetails(courseId)
    }

    override suspend fun createCourse(request: CreateCourseRequestDto): CourseCreationResponseDto {
        return coursesApiService.createCourse(request)
    }

    override suspend fun updateCourse(courseId: String, request: UpdateCourseRequestDto): CourseDto {
        return coursesApiService.updateCourse(courseId, request)
    }

    override suspend fun deleteCourse(courseId: String): ActionStatusResponseDto {
        return coursesApiService.deleteCourse(courseId)
    }

    override suspend fun getCourseMaterials(courseId: String): List<CourseMaterialDto> {
        return coursesApiService.getCourseMaterials(courseId)
    }

    override suspend fun createMaterialMetadata(courseId: String, request: MaterialUploadRequestDto): MaterialUploadResponseDto {
        return coursesApiService.createMaterialMetadata(courseId, request)
    }

    override suspend fun uploadMaterialFile(materialId: String, file: MultipartBody.Part): FileUploadResponseDto {
        return coursesApiService.uploadMaterialFile(materialId, file)
    }

    override suspend fun deleteMaterial(courseId: String, materialId: String): ActionStatusResponseDto {
        return coursesApiService.deleteMaterial(courseId, materialId)
    }

    override suspend fun addCourseEvent(courseId: String, request: AddEventRequestDto): AddEventResponseDto {
        return coursesApiService.addCourseEvent(courseId, request)
    }

    override suspend fun getCourseTasks(courseId: String): CourseTasksResponseDto {
        return tasksApiService.getCourseTasks(courseId)
    }
}
