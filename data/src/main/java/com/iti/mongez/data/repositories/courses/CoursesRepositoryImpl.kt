package com.iti.mongez.data.repositories.courses

import com.iti.mongez.data.dtos.coursesdtos.CreateCourseRequestDto
import com.iti.mongez.data.dtos.coursesdtos.MaterialUploadRequestDto
import com.iti.mongez.data.dtos.coursesdtos.UpdateCourseRequestDto
import com.iti.mongez.domain.core.Result
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.CoursesApiService
import com.iti.mongez.domain.core.Alert
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.courses.model.CourseActionResponse
import com.iti.mongez.domain.courses.model.CourseMaterial
import com.iti.mongez.domain.courses.repository.CoursesRepository
import okhttp3.MultipartBody
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(
    private val apiService: CoursesApiService
) : CoursesRepository {

    override suspend fun getCourses(): Result<List<Course>> = safeApi {
        apiService.getCourses().map { it.toDomain() }
    }

    override suspend fun getCourseDetails(courseId: String): Result<Course> = safeApi {
        apiService.getCourseDetails(courseId).toDomain()
    }

    override suspend fun createCourse(
        name: String,
        courseCode: String,
        imageUrl: String,
        startDate: String,
        examDate: String,
        courseType: String,
        materialUrl: String?
    ): Result<CourseCreationResult> = safeApi {
        val request = CreateCourseRequestDto(
            name = name,
            courseCode = courseCode,
            imageUrl = imageUrl,
            startDate = startDate,
            examDate = examDate,
            courseType = courseType,
            materialUrl = materialUrl
        )

        val response = apiService.createCourse(request)
        response.toDomain()
    }

    override suspend fun updateCourse(courseId: String, name: String, isHidden: Boolean): Result<CourseActionResponse<Course>> = safeApi {
        val response = apiService.updateCourse(courseId, UpdateCourseRequestDto(name, isHidden))
        CourseActionResponse(data = response.toDomain(), alert = null)
    }

    override suspend fun deleteCourse(courseId: String): Result<CourseActionResponse<Unit>> = safeApi {
        val response = apiService.deleteCourse(courseId)
        CourseActionResponse(data = Unit, alert = response.alert?.let { Alert(it.message.orEmpty()) })
    }

    override suspend fun getCourseMaterials(courseId: String): Result<List<CourseMaterial>> = safeApi {
        apiService.getCourseMaterials(courseId).map { it.toDomain() }
    }

    override suspend fun uploadCourseMaterial(
        courseId: String,
        fileName: String,
        contentType: String,
        fileSizeBytes: Long, // Kept for interface compatibility, even if unused by API
        pageCount: Int,      // Kept for interface compatibility
        fileBytes: ByteArray
    ): Result<CourseActionResponse<Unit>> {
        return try {
            // 1. Create the request body from the file bytes
            val mediaType = MediaType.parse(contentType) ?: MediaType.parse("application/octet-stream")
            val requestBody = RequestBody.create(mediaType, fileBytes)

            // 2. Wrap it in a Multipart part matching the "file" key in your Postman request
            val multipartBody = MultipartBody.Part.createFormData("file", fileName, requestBody)

            // 3. Send the direct POST request
            val uploadResponse = apiService.uploadCourseMaterial(courseId, multipartBody)

            if (uploadResponse.isSuccessful) {
                Result.Success(CourseActionResponse(data = Unit, alert = null))
            } else {
                Result.Failure(Exception("Upload failed: Error ${uploadResponse.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun deleteCourseMaterial(courseId: String, materialId: String): Result<CourseActionResponse<Unit>> = safeApi {
        val response = apiService.deleteMaterial(courseId, materialId)
        CourseActionResponse(data = Unit, alert = response.alert?.let { Alert(it.message.orEmpty()) })
    }
}