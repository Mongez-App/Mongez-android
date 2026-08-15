package com.iti.mongez.presentation.courses.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.mongez.designsystem.screens.courses.CourseCard
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
            bottom = Theme.spacing.giant + Theme.spacing.lg
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