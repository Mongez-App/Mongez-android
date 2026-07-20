package com.iti.mongez.presentation.coursedetails.uistate


data class DocumentItem(
    val id: String,
    val title: String,
    val pageCount: Int,
    val fileSize: String,
    val fileExtension: String = "PDF"
)
data class TaskItem(
    val id: String,
    val title: String,
    val duration: String,
    val priority: String, // HIGH, MEDIUM, LOW
    val isCompleted: Boolean,
    val isToday: Boolean
)

data class CourseDetailsUiState(
    val isLoading: Boolean = false,
    val courseTitle: String = "Operating Systems",
    val selectedTabIndex: Int = 0,
    val tabs: List<String> = listOf("Materials", "Tasks"),
    val materials: List<DocumentItem> = emptyList(),
    // Tasks Tab Data
    val completedTasks: Int = 3,
    val totalTasks: Int = 5,
    val progressPercentage: Int = 60,
    val taskFilters: List<String> = listOf("All", "Pending", "Completed", "High", "Medium", "Low"),
    val selectedTaskFilterIndex: Int = 0,
    val tasks: List<TaskItem> = emptyList()
)