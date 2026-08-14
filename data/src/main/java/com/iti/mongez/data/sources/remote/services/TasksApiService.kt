package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.coursesdtos.CourseTaskDto
import retrofit2.http.GET
import retrofit2.http.Path

interface TasksApiService {
    @GET("courses/{course_id}/tasks")
    suspend fun getCourseTasks(
        @Path("course_id") courseId: String
    ): List<CourseTaskDto>
}
