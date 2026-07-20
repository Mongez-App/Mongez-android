package com.iti.mongez.presentation.coursedetails.uistate


data class DocumentItem(
    val id: String,
    val title: String,
    val pageCount: Int,
    val fileSize: String,
    val fileExtension: String = "PDF"
)

data class CourseDetailsUiState(
    val isLoading: Boolean = false,
    val courseTitle: String = "Operating Systems",
    val selectedTabIndex: Int = 0,
    val tabs: List<String> = listOf("Materials", "Tasks"),
    val materials: List<DocumentItem> = emptyList()
)