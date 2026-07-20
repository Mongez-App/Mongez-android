package com.iti.mongez.domain.courses.model

import com.iti.mongez.domain.core.Alert

data class CourseActionResponse<T>(
    val data: T,
    val alert: Alert? = null
)
