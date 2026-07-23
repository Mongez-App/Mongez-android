package com.iti.mongez.data.repositories.courses

import com.iti.mongez.data.dtos.CreateCourseRequestDto
import com.iti.mongez.data.dtos.MaterialUploadRequestDto
import com.iti.mongez.data.dtos.UpdateCourseRequestDto
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
import javax.inject.Inject
import okhttp3.MediaType
import okhttp3.RequestBody

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
        name: String, courseCode: String, startDate: String, examDate: String, hasMaterials: Boolean
    ): Result<CourseCreationResult> = safeApi {
        val request = CreateCourseRequestDto(name, courseCode, startDate, examDate, hasMaterials)
        apiService.createCourse(request).toDomain()
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

    // Updated signature to include pageCount
    override suspend fun uploadCourseMaterial(
        courseId: String,
        fileName: String,
        contentType: String,
        fileSizeBytes: Long,
        pageCount: Int,
        fileBytes: ByteArray
    ): Result<CourseActionResponse<Unit>> {

        // Step 1: Tell the server to expect a file and get the URL
        val step1Response = safeApi {
            apiService.requestMaterialUploadUrl(
                courseId,
                MaterialUploadRequestDto(fileName, contentType, fileSizeBytes, pageCount)
            )
        }

        return when (step1Response) {
            is Result.Success -> {
                val uploadUrl = step1Response.data.uploadUrl ?: return Result.Failure(Exception("Server did not return an upload URL"))

                // Format the URL to prevent double slashes and missing domains
                val baseUrl = "https://api-gateway-production-3fd0.up.railway.app"
                val finalUrl = if (uploadUrl.startsWith("/")) "$baseUrl$uploadUrl" else uploadUrl

                // Step 2: Upload the physical file to the generated URL via Retrofit
                try {
                    val mediaType = MediaType.parse(contentType)
                    val requestBody = RequestBody.create(mediaType, fileBytes)
                    val multipartBody = MultipartBody.Part.createFormData("file", fileName, requestBody)

                    val uploadResponse = apiService.uploadMaterialFile(finalUrl, multipartBody)

                    if (uploadResponse.isSuccessful) {
                        Result.Success(CourseActionResponse(data = Unit, alert = null))
                    } else {
                        // The server rejected the file upload
                        Result.Failure(Exception("Upload failed: Error ${uploadResponse.code()}"))
                    }
                } catch (e: Exception) {
                    Result.Failure(e)
                }
            }
            is Result.Failure -> {
                // Step 1 Failed
                step1Response
            }
            else -> Result.Failure(Exception("Unknown error occurred during upload initialization"))
        }
    }

    override suspend fun deleteCourseMaterial(courseId: String, materialId: String): Result<CourseActionResponse<Unit>> = safeApi {
        val response = apiService.deleteMaterial(courseId, materialId)
        CourseActionResponse(data = Unit, alert = response.alert?.let { Alert(it.message.orEmpty()) })
    }
}