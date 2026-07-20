package com.iti.mongez.presentation.courses.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.iti.mongez.designsystem.components.fab.AppFab
import com.iti.mongez.designsystem.components.search.AppSearchBar
import com.iti.mongez.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.courses.CourseCard
import com.iti.mongez.designsystem.theme.Theme
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
    onIntent: (CoursesIntent) -> Unit, // Unified callback for all actions
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
                    onClick = {
                        onIntent(CoursesIntent.ToggleAddCourseSheet)
                    }
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
                    onQueryChange = {
                        onIntent(CoursesIntent.SearchQueryChanged(it))
                    },
                    onFilterClick = {
                        onIntent(CoursesIntent.FilterClicked)
                    }
                )

                Spacer(
                    modifier = Modifier.height(Theme.spacing.lg)
                )

                if (state.isLoading && state.allCourses.isEmpty()) {

                    // Fix 1: Center the circular indicator vertically and horizontally
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                }else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            Theme.spacing.lg
                        ),
                        contentPadding = PaddingValues(
                            bottom = Theme.spacing.giant + Theme.spacing.lg
                        )
                    ) {

                        items(
                            state.filteredCourses,
                            key = { it.id }
                        ) { course ->

                            // Fix 2: Smooth out search filtering, additions, and removals animations
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                            ) {
                                CourseCard(
                                    title = course.name,
                                    progress = course.completionPercentage / 100f,
                                    onClick = {
                                        onCourseClick(course.id) // <-- TRIGGER NAVIGATION
                                    },
                                    imagePainter = rememberAsyncImagePainter(
                                        "https://www.atmajaya.ac.id/en/media/coursera.png"
                                    )
                                )
                            }
                        }
                    }
                }
            }

            if (state.isAddCourseSheetVisible) {

                AppBottomSheet(
                    onDismiss = {
                        onIntent(CoursesIntent.ToggleAddCourseSheet)
                    },
                    title = stringResource(R.string.add_new_course)
                ) {

                    AddCourseSheetContent(
                        isLoading = state.isCreatingCourse,
                        onAddCourse = { name, code, startDate, examDate, hasMaterials ->

                            onIntent(
                                CoursesIntent.CreateCourse(
                                    name = name,
                                    courseCode = code,
                                    startDate = startDate,
                                    examDate = examDate,
                                    hasMaterials = hasMaterials,
                                )
                            )
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

                AppSnackbarContent(
                    message = message,
                    type = topSnackbarType
                )
            }
        }
    }
}