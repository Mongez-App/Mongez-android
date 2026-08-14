package com.iti.mongez.presentation.courses.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.iti.mongez.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.designsystem.components.fab.AppFab
import com.iti.mongez.designsystem.components.search.AppSearchBar
import com.iti.mongez.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.courses.CourseCard
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.courses.contract.CoursesIntent
import com.iti.mongez.presentation.courses.components.AddCourseSheetContent
import com.iti.mongez.presentation.courses.uiState.CoursesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreenContent(
    state: CoursesState,
    innerPadding: PaddingValues,
    topSnackbarMessage: String?,
    topSnackbarType: AppSnackbarType,
    onIntent: (CoursesIntent) -> Unit,
    onCourseClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Scaffold(
            floatingActionButton = {
                AppFab(
                    modifier = Modifier.padding(bottom = Theme.spacing.lg),
                    onClick = { onIntent(CoursesIntent.ToggleAddCourseSheet) }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(Theme.spacing.lg)
            ) {
                Text(
                    text = stringResource(R.string.my_courses),
                    style = Theme.typography.headline.medium,
                    color = Theme.colorScheme.text.primary,
                    modifier = Modifier.padding(bottom = Theme.spacing.lg)
                )

                AppSearchBar(
                    query = state.searchQuery,
                    onQueryChange = { onIntent(CoursesIntent.SearchQueryChanged(it)) },
                    onFilterClick = { onIntent(CoursesIntent.FilterClicked) }
                )

                Spacer(modifier = Modifier.height(Theme.spacing.lg))

                if (state.isLoading && state.allCourses.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (!state.isLoading && state.filteredCourses.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (state.searchQuery.isNotEmpty()) {
                                stringResource(R.string.no_courses_match_search)
                            } else {
                                stringResource(R.string.no_courses_empty_state)
                            },
                            style = Theme.typography.body.large,
                            color = Theme.colorScheme.text.secondary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg),
                        contentPadding = PaddingValues(bottom = Theme.spacing.giant + Theme.spacing.lg)
                    ) {
                        items(state.filteredCourses, key = { it.id }) { course ->
                            Box(modifier = Modifier.fillMaxWidth().animateItem()) {
                                CourseCard(
                                    title = course.name,
                                    progress = course.completionPercentage / 100f,
                                    onClick = { onCourseClick(course.id) },
                                    imagePainter = rememberAsyncImagePainter(course.imageUrl?.takeIf { it.isNotBlank() } ?: "https://www.atmajaya.ac.id/en/media/coursera.png")
                                )
                            }
                        }
                    }
                }
            }

            if (state.courseToDeleteId != null) {
                AppConfirmationDialog(
                    title = stringResource(R.string.delete_course_title),
                    description = stringResource(R.string.delete_course_dialog_description),
                    primaryActionText = stringResource(R.string.action_delete),
                    onPrimaryAction = {
                        onIntent(CoursesIntent.ConfirmDeleteCourse(state.courseToDeleteId))
                    },
                    onDismiss = {
                        onIntent(CoursesIntent.DismissDeleteConfirmation)
                    },
                    secondaryActionText = stringResource(R.string.action_cancel),
                    onSecondaryAction = {
                        onIntent(CoursesIntent.DismissDeleteConfirmation)
                    }
                )
            }

            if (state.isAddCourseSheetVisible) {
                AppBottomSheet(
                    onDismiss = { onIntent(CoursesIntent.ToggleAddCourseSheet) },
                    title = stringResource(R.string.add_new_course)
                ) {
                    val emptyFieldsError = stringResource(R.string.error_empty_fields)

                    AddCourseSheetContent(
                        isLoading = state.isCreatingCourse,
                        onAddCourse = { name, code, imageUrl, startDate, examDate, materials, isOnlineCourse, materialUrl ->

                            val isUrlValid = materialUrl?.let { android.util.Patterns.WEB_URL.matcher(it).matches() } ?: true

                            if (name.isBlank() || code.isBlank() || startDate.isBlank() || examDate.isBlank() || (isOnlineCourse && materialUrl.isNullOrBlank())) {
                                onIntent(CoursesIntent.ShowSnackbar(message = emptyFieldsError, type = AppSnackbarType.Error))
                            } else if (isOnlineCourse && !isUrlValid) {
                                onIntent(CoursesIntent.ShowSnackbar(message = "Please enter a valid Course URL", type = AppSnackbarType.Error))
                            } else {
                                onIntent(
                                    CoursesIntent.CreateCourse(
                                        name = name,
                                        courseCode = code,
                                        imageUrl = imageUrl,
                                        startDate = startDate,
                                        examDate = examDate,
                                        materials = materials,
                                        courseType = if (isOnlineCourse) "URL_COURSE" else "MATERIAL_COURSE",
                                        materialUrl = if (isOnlineCourse) materialUrl else null
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = topSnackbarMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Theme.spacing.md)
                .padding(top = Theme.spacing.xxl)
        ) {
            topSnackbarMessage?.let { message ->
                AppSnackbarContent(message = message, type = topSnackbarType)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CoursesScreenContentPreview() {
    val sampleCourses = listOf(
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
        )
    )

    MongezTheme {
        CoursesScreenContent(
            state = CoursesState(
                allCourses = sampleCourses,
                filteredCourses = sampleCourses
            ),
            innerPadding = PaddingValues(0.dp),
            topSnackbarMessage = null,
            topSnackbarType = AppSnackbarType.Info,
            onIntent = {},
            onCourseClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CoursesScreenContentDarkPreview() {
    val sampleCourses = listOf(
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
        )
    )

    MongezTheme(darkTheme = true) {
        CoursesScreenContent(
            state = CoursesState(
                allCourses = sampleCourses,
                filteredCourses = sampleCourses
            ),
            innerPadding = PaddingValues(0.dp),
            topSnackbarMessage = null,
            topSnackbarType = AppSnackbarType.Info,
            onIntent = {},
            onCourseClick = {}
        )
    }
}