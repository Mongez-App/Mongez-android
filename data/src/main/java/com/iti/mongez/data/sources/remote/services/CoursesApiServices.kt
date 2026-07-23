package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.coursesdtos.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface CoursesApiService {
    @GET("courses")
    suspend fun getCourses(): List<CourseDto>

    @GET("courses/{course_id}")
    suspend fun getCourseDetails(@Path("course_id") courseId: String): CourseDto

    @POST("courses")
    suspend fun createCourse(@Body request: CreateCourseRequestDto): CourseCreationResponseDto

    @PATCH("courses/{course_id}")
    suspend fun updateCourse(
        @Path("course_id") courseId: String,
        @Body request: UpdateCourseRequestDto
    ): CourseDto // Wait, check API response, it might need a wrapper if it returns an alert

    @DELETE("courses/{course_id}")
    suspend fun deleteCourse(@Path("course_id") courseId: String): ActionStatusResponseDto

    @GET("courses/{course_id}/materials")
    suspend fun getCourseMaterials(@Path("course_id") courseId: String): List<CourseMaterialDto>

    @POST("courses/{course_id}/materials")
    suspend fun requestMaterialUploadUrl(
        @Path("course_id") courseId: String,
        @Body request: MaterialUploadRequestDto
    ): MaterialUploadResponseDto

    @DELETE("courses/{course_id}/materials/{material_id}")
    suspend fun deleteMaterial(
        @Path("course_id") courseId: String,
        @Path("material_id") materialId: String
    ): ActionStatusResponseDto

    @Multipart
    @POST // Note: If your backend strictly uses PUT for file uploads, change this to @PUT
    suspend fun uploadMaterialFile(
        @Url url: String,
        @Part file: MultipartBody.Part
    ): Response<Unit>
}