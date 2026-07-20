package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.CourseDto
import com.iti.mongez.data.dtos.CreateCourseRequestDto
import com.iti.mongez.data.dtos.CreateCourseResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CoursesApiService {
    @GET("courses")
    suspend fun getCourses(): List<CourseDto>

    @POST("courses")
    suspend fun createCourse(
        @Body request: CreateCourseRequestDto
    ): CreateCourseResponseDto
}