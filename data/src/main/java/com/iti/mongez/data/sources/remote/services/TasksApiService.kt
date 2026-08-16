package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.chatdtos.ChatMessagesResponseDto
import com.iti.mongez.data.dtos.chatdtos.SendChatMessageRequestDto
import com.iti.mongez.data.dtos.chatdtos.SendChatMessageResponseDto
import com.iti.mongez.data.dtos.coursesdtos.CourseTaskDto
import com.iti.mongez.data.dtos.coursesdtos.CourseTasksResponseDto
import com.iti.mongez.data.dtos.tasksdtos.UpdateTaskRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TasksApiService {
    @GET("courses/{course_id}/tasks")
    suspend fun getCourseTasks(
        @Path("course_id") courseId: String
    ): CourseTasksResponseDto

    @PATCH("tasks/{task_id}")
    suspend fun updateTask(
        @Path("task_id") taskId: String,
        @Body request: UpdateTaskRequestDto
    ): CourseTaskDto

    @POST("tasks/{task_id}/chat")
    suspend fun sendChatMessage(
        @Path("task_id") taskId: String,
        @Body request: SendChatMessageRequestDto
    ): SendChatMessageResponseDto

    @GET("tasks/{task_id}/chat/messages")
    suspend fun getChatMessages(
        @Path("task_id") taskId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 30
    ): ChatMessagesResponseDto

    @DELETE("tasks/{task_id}/chat")
    suspend fun deleteChatMessages(
        @Path("task_id") taskId: String
    )
}
