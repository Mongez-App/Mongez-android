package com.iti.mongez.presentation.coursedetails.uistate

import androidx.annotation.StringRes

import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.core.models.TaskItem

data class DocumentItem(
    val id: String,
    val title: String,
    val pageCount: Int,
    val fileSize: String,
    val fileExtension: String = "PDF",
    val fileUri: String? = null

)



data class CourseDetailsUiState(
    val isLoading: Boolean = false,
    val courseTitle: String = "",
    val imageUrl: String = "", // Added for editing

    val courseCode: String = "",
    val courseType: String = "",
    val startDate: String = "",
    val examDate: String = "",
    val materialUrl: String? = null,

    val selectedTabIndex: Int = 0,
    @StringRes val tabs: List<Int> = listOf(
        R.string.tab_materials,
        R.string.tab_tasks
    ),
    val materials: List<DocumentItem> = emptyList(),
    // Tasks Tab Data
    val completedTasks: Int = 3,
    val totalTasks: Int = 5,
    val progressPercentage: Int = 60,
    @StringRes val taskFilters: List<Int> = listOf(
        R.string.filter_all,
        R.string.filter_pending,
        R.string.filter_completed,
        R.string.filter_high,
        R.string.filter_medium,
        R.string.filter_low
    ),
    val selectedTaskFilterIndex: Int = 0,
    val tasks: List<TaskItem> = emptyList(),
    val isDeleteDialogVisible: Boolean = false
)