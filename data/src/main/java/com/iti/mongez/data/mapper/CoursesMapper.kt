package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.CourseDto
import com.iti.mongez.data.dtos.CreateCourseResponseDto
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult

fun CourseDto.toDomain() = Course(
    id = id,
    name = name,
    courseCode = courseCode,
    startDate = startDate,
    examDate = examDate,
    hasMaterials = hasMaterials,
    completionPercentage = completionPercentage
)

fun CreateCourseResponseDto.toDomain() = CourseCreationResult(
    course = Course(
        id = id,
        name = name,
        courseCode = courseCode,
        startDate = startDate,
        examDate = examDate,
        hasMaterials = hasMaterials,
        completionPercentage = completionPercentage
    ),
    alertMessage = alert?.message
)