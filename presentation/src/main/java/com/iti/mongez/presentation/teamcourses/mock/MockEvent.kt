package com.iti.mongez.presentation.teamcourses.mock

import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.presentation.teamcourses.model.TrackEvent

data class MockEvent(
    val subject: String,
    val taskType: String,
    val timeLeft: String,
    val tintColor: Color
)


object MockData {
    // Change this to 'true' to see the empty states, or 'false' to see the populated grid
    const val SHOW_EMPTY_STATE = false
    fun getMockCourses(): List<Course> {
        return listOf(
            Course(
                id = "course_1",
                name = "Android Native Development",
                courseCode = "AND-101",
                imageUrl = "https://www.gizmochina.com/wp-content/uploads/2020/04/Android-Logo-2019-1068x601.jpg",
                startDate = "2026-06-01",
                examDate = "2026-08-01",
                hasMaterials = true,
                completionPercentage = 0.75f,
                isHidden = false,
                courseType = "STANDARD",
                materialUrl = null
            ),
            Course(
                id = "course_2",
                name = "Flutter Cross-Platform",
                courseCode = "FLUT-201",
                imageUrl = "https://i.pinimg.com/736x/6b/36/4e/6b364e3349e9c89ba221b808552f9a52.jpg",
                startDate = "2026-06-15",
                examDate = "2026-08-15",
                hasMaterials = true,
                completionPercentage = 0.40f,
                isHidden = false,
                courseType = "STANDARD",
                materialUrl = null
            ),
            Course(
                id = "course_3",
                name = "Advanced Jetpack Compose",
                courseCode = "COMPOSE-301",
                imageUrl = "https://androidayuda.com/wp-content/uploads/2025/02/jetpack-compose.png",
                startDate = "2026-07-01",
                examDate = "2026-09-01",
                hasMaterials = false,
                completionPercentage = 0.10f,
                isHidden = false,
                courseType = "URL_COURSE",
                materialUrl = "https://iti.gov.eg/courses/compose"
            )
        )
    }

    fun getMockEvents(): List<TrackEvent> {
        if (SHOW_EMPTY_STATE) return emptyList()

        return listOf(
            TrackEvent(
                id = "1",
                courseName = "Networks",
                eventName = "Assignment",
                timeRemaining = "Tomorrow",
                themeColor = Color(0xFFE53935) // Red/Error
            ),
            TrackEvent(
                id = "2",
                courseName = "Operating Systems",
                eventName = "Midterm",
                timeRemaining = "4 days left",
                themeColor = Color(0xFF43A047) // Green/Success
            ),
            TrackEvent(
                id = "3",
                courseName = "Mobile Native",
                eventName = "Project",
                timeRemaining = "1 week left",
                themeColor = Color(0xFF1E88E5) // Blue/Primary
            ),
            TrackEvent(
                id = "4",
                courseName = "Data Structures",
                eventName = "Quiz",
                timeRemaining = "Today",
                themeColor = Color(0xFFE53935)
            )
        )
    }
}