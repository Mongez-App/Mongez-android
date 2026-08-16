package com.iti.mongez.presentation.courses.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.screens.courses.CourseCard
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.domain.courses.model.Course

@Composable
fun CoursesList(
    courses: List<Course>,
    onCourseClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg),
        contentPadding = PaddingValues(
            vertical = Theme.spacing.lg
        )
    ) {
        items(
            items = courses,
            key = { it.id }
        ) { course ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem()
            ) {
                CourseCard(
                    title = course.name,
                    progress = course.completionPercentage / 100f,
                    onClick = {
                        onCourseClick(course.id)
                    },
                    imageUrl = course.imageUrl
                        ?.takeIf { it.isNotBlank() }
                        ?: "https://www.atmajaya.ac.id/en/media/coursera.png"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CoursesListPreview() {
    MongezTheme {
        CoursesList(
            courses = sampleCourses,
            onCourseClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Courses List - Dark Mode")
@Composable
private fun CoursesListDarkPreview() {
    MongezTheme(darkTheme = true) {
        CoursesList(
            courses = sampleCourses,
            onCourseClick = {}
        )
    }
}

private val sampleCourses = listOf(
    Course(
        id = "1",
        name = "Mobile Development",
        courseCode = "CS402",
        imageUrl = null,
        startDate = "2023-10-01",
        examDate = "2024-01-15",
        hasMaterials = true,
        completionPercentage = 65f
    ),
    Course(
        id = "2",
        name = "Web Security",
        courseCode = "CS305",
        imageUrl = null,
        startDate = "2023-10-05",
        examDate = "2024-01-20",
        hasMaterials = false,
        completionPercentage = 30f
    ),
    Course(
        id = "3",
        name = "Cloud Computing",
        courseCode = "CS408",
        imageUrl = null,
        startDate = "2023-10-10",
        examDate = "2024-01-25",
        hasMaterials = true,
        completionPercentage = 85f
    )
)