package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.CourseCreationResponseDto
import com.iti.mongez.data.dtos.CourseDto
import com.iti.mongez.data.dtos.CourseMaterialDto
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.courses.model.CourseMaterial
import java.time.Instant

fun CourseDto.toDomain() = Course(
    id = this.id.orEmpty(),
    name = this.name.orEmpty(),
    courseCode = this.courseCode.orEmpty(),
    startDate = this.startDate.orEmpty(),
    examDate = this.examDate.orEmpty(),
    hasMaterials = this.hasMaterials ?: false,
    completionPercentage = this.completionPercentage ?: 0f,
    isHidden = this.isHidden ?: false
)

fun CourseCreationResponseDto.toDomain() = CourseCreationResult(
    course = Course(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        courseCode = this.courseCode.orEmpty(),
        startDate = this.startDate.orEmpty(),
        examDate = this.examDate.orEmpty(),
        hasMaterials = this.hasMaterials ?: false,
        completionPercentage = this.completionPercentage ?: 0f,
        isHidden = false
    ),
    alertMessage = this.alert?.message
)

fun CourseMaterialDto.toDomain() = CourseMaterial(
    id = this.materialId.orEmpty(),
    name = this.name.orEmpty(),
    pageCount = this.pageCount ?: 0,
    fileSizeMb = this.fileSizeMb ?: 0.0,
    status = this.status.orEmpty(),
    uploadedAt = try {
        Instant.parse(this.uploadedAt)
    } catch (e: Exception) {
        Instant.now()
    }
)
