package com.iti.mongez.domain.courses.model

import com.iti.mongez.domain.core.model.TaskPriority

data class CourseTask(
    val id: String,
    val courseId: String,
    val title: String,
    val durationMinutes: Int,
    val priority: TaskPriority,
    val isCompleted: Boolean,
    val scheduledDate: String
)
