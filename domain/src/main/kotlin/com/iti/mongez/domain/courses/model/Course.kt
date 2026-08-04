package com.iti.mongez.domain.courses.model


data class Course(
    val id: String,
    val name: String,
    val courseCode: String,
    val imageUrl: String?,
    val startDate: String,
    val examDate: String,
    val hasMaterials: Boolean,
    val completionPercentage: Float,
    val isHidden: Boolean = false,
    val courseType: String = "",     // Added
    val materialUrl: String? = null
)

data class CourseCreationResult(
    val course: Course,
    val alertMessage: String?
)