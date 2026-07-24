package com.iti.mongez.presentation.courses.uiState

import com.iti.mongez.domain.courses.model.Course

data class CoursesState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val allCourses: List<Course> = emptyList(),
    val filteredCourses: List<Course> = emptyList(),
    val isAddCourseSheetVisible: Boolean = false,
    val isCreatingCourse: Boolean = false,
    val courseToDeleteId: String? = null,
    val isDeletingCourse: Boolean = false
)