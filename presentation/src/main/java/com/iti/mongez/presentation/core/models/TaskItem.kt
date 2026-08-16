package com.iti.mongez.presentation.core.models

import com.iti.mongez.domain.core.model.TaskPriority

data class TaskItem(
    val id: String,
    val courseId: String,
    val title: String,
    val duration: String,
    val durationMinutes: Int,
    val priority: TaskPriority,
    val isCompleted: Boolean,
    val isToday: Boolean = false
)

val TaskPriority.colorRes: androidx.compose.ui.graphics.Color
    @androidx.compose.runtime.Composable
    get() = when (this) {
        TaskPriority.HIGH -> com.iti.mongez.designsystem.theme.Theme.colorScheme.state.error
        TaskPriority.MEDIUM -> com.iti.mongez.designsystem.theme.Theme.colorScheme.state.warning
        TaskPriority.LOW -> com.iti.mongez.designsystem.theme.Theme.colorScheme.state.success
    }

val TaskPriority.textRes: Int
    get() = when (this) {
        TaskPriority.HIGH -> com.iti.mongez.designsystem.R.string.priority_high
        TaskPriority.MEDIUM -> com.iti.mongez.designsystem.R.string.priority_medium
        TaskPriority.LOW -> com.iti.mongez.designsystem.R.string.priority_low
    }
