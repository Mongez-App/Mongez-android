package com.iti.mongez.data.repositories.courses

import com.iti.mongez.data.dtos.coursesdtos.CreateCourseRequestDto
import com.iti.mongez.data.dtos.coursesdtos.MaterialUploadRequestDto
import com.iti.mongez.data.dtos.coursesdtos.UpdateCourseRequestDto
import com.iti.mongez.domain.core.Result
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.interfaces.CoursesRemoteDataSource
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
    private val remoteDataSource: CoursesRemoteDataSource
) : CoursesRepository {

    override suspend fun getCourses(): Result<List<Course>> = safeApi {
        remoteDataSource.getCourses().map { it.toDomain() }
    }

    override suspend fun getCourseDetails(courseId: String): Result<Course> = safeApi {
        remoteDataSource.getCourseDetails(courseId).toDomain()
    }

    override suspend fun getCourseTasks(courseId: String): Result<List<com.iti.mongez.domain.courses.model.CourseTask>> = safeApi {
        val response = remoteDataSource.getCourseTasks(courseId)
        response.data?.map { it.toDomain() } ?: emptyList()
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

        val response = remoteDataSource.createCourse(request)
        response.toDomain()
    }

    override suspend fun updateCourse(courseId: String, name: String, imageUrl: String, isHidden: Boolean): Result<CourseActionResponse<Course>> = safeApi {
        val response = remoteDataSource.updateCourse(courseId, UpdateCourseRequestDto(name, imageUrl, isHidden))
        CourseActionResponse(data = response.toDomain(), alert = null)
    }

    override suspend fun deleteCourse(courseId: String): Result<CourseActionResponse<Unit>> = safeApi {
        val response = remoteDataSource.deleteCourse(courseId)
        CourseActionResponse(data = Unit, alert = response.alert?.let { Alert(it.message.orEmpty()) })
    }

    override suspend fun getCourseMaterials(courseId: String): Result<List<CourseMaterial>> = safeApi {
        remoteDataSource.getCourseMaterials(courseId).map { it.toDomain() }
    }

    override suspend fun uploadCourseMaterial(
        courseId: String,
        fileName: String,
        contentType: String,
        fileSizeBytes: Long,
        pageCount: Int,
        fileBytes: ByteArray,
        deviceFileUri: String?
    ): Result<CourseActionResponse<Unit>> = safeApi {

        // Step 1: Upload metadata
        val metadataRequest = MaterialUploadRequestDto(
            fileName = fileName,
            contentType = contentType,
            fileSizeBytes = fileSizeBytes,
            pageCount = pageCount,
            deviceFileUri = deviceFileUri
        ) //[cite: 18, 20]
        val metadataResponse = remoteDataSource.createMaterialMetadata(courseId, metadataRequest) //[cite: 18]

        val materialId = metadataResponse.materialId
            ?: throw IllegalStateException("Material ID was not returned by the server.") //[cite: 18]

        // EXTRACT THE UPLOAD URL
        val uploadUrl = metadataResponse.uploadUrl
            ?: throw IllegalStateException("Upload URL was not returned by the server.") //[cite: 21]

        // Step 2: Upload actual file binary
        val mediaType = MediaType.parse(contentType) ?: MediaType.parse("application/octet-stream") //[cite: 18]
        val requestBody = RequestBody.create(mediaType, fileBytes) //[cite: 18]
        val multipartBody = MultipartBody.Part.createFormData("file", fileName, requestBody) //[cite: 18]

        // USE THE URL AND ADD EXPLICIT ERROR HANDLING
        val uploadResult = try {
            remoteDataSource.uploadMaterialFile(uploadUrl, multipartBody)
        } catch (e: Exception) {
            android.util.Log.e("UploadError", "Step 2 Failed: ${e.message}", e) // ADD THIS LINE
            throw IllegalStateException("Metadata uploaded successfully, but the actual file failed to upload: ${e.message}")
        }

        // Validate the server status from the second response (matches "PROCESSING" from Postman)
        if (uploadResult.status != "PROCESSING") { //[cite: 19]
            throw IllegalStateException("File upload failed on server. Status: ${uploadResult.status}")
        }

        CourseActionResponse(
            data = Unit,
            alert = uploadResult.message?.let { Alert(it) } //[cite: 18, 19]
        )
    }

    override suspend fun deleteCourseMaterial(courseId: String, materialId: String): Result<CourseActionResponse<Unit>> = safeApi {
        val response = remoteDataSource.deleteMaterial(courseId, materialId)
        CourseActionResponse(data = Unit, alert = response.alert?.let { Alert(it.message.orEmpty()) })
    }
}