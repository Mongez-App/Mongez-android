package com.iti.mongez.presentation.courses

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.iti.mongez.designsystem.components.fab.AppFab
import com.iti.mongez.designsystem.components.search.AppSearchBar
import com.iti.mongez.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.courses.CourseCard
import com.iti.mongez.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    innerPadding: PaddingValues,
    viewModel: CoursesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Track both the message and the type for the top banner
    var topSnackbarMessage by remember { mutableStateOf<String?>(null) }
    var topSnackbarType by remember { mutableStateOf(AppSnackbarType.Info) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CoursesEffect.ShowSnackbar -> {
                    topSnackbarMessage = effect.message
                    topSnackbarType = effect.type
                }
            }
        }
    }

    // Auto-dismiss the top banner after 3 seconds
    LaunchedEffect(topSnackbarMessage) {
        if (topSnackbarMessage != null) {
            kotlinx.coroutines.delay(3000L)
            topSnackbarMessage = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Scaffold(
            // Bottom snackbar host removed since all snackbars now use the top banner
            floatingActionButton = {
                AppFab(
                    modifier = Modifier.padding(bottom = Theme.spacing.lg),
                    onClick = { viewModel.handleIntent(CoursesIntent.ToggleAddCourseSheet) }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(Theme.spacing.lg)
            ) {
                Text(
                    text = "My Courses",
                    style = Theme.typography.headline.medium,
                    color = Theme.colorScheme.text.primary,
                    modifier = Modifier.padding(bottom = Theme.spacing.lg)
                )

                AppSearchBar(
                    query = state.searchQuery,
                    onQueryChange = { viewModel.handleIntent(CoursesIntent.SearchQueryChanged(it)) },
                    onFilterClick = { viewModel.handleIntent(CoursesIntent.FilterClicked) }
                )

                Spacer(modifier = Modifier.height(Theme.spacing.lg))

                if (state.isLoading && state.allCourses.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.padding(Theme.spacing.lg))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(state.filteredCourses, key = { it.id }) { course ->
                            CourseCard(
                                title = course.name,
                                progress = course.completionPercentage / 100f,
                                onClick = { /* Navigate to course details */ },
                                imagePainter = rememberAsyncImagePainter("https://www.atmajaya.ac.id/en/media/coursera.png")
                            )
                        }
                    }
                }
            }

            if (state.isAddCourseSheetVisible) {
                AppBottomSheet(
                    onDismiss = { viewModel.handleIntent(CoursesIntent.ToggleAddCourseSheet) },
                    title = "Add New Course"
                ) {
                    AddCourseSheetContent(
                        isLoading = state.isCreatingCourse,
                        onAddCourse = { name, code, startDate, examDate, hasMaterials ->
                            viewModel.handleIntent(
                                CoursesIntent.CreateCourse(
                                    name,
                                    code,
                                    startDate,
                                    examDate,
                                    hasMaterials
                                )
                            )
                        }
                    )
                }
            }
        }

        // Top animated banner handles ALL snackbar types dynamically
        AnimatedVisibility(
            visible = topSnackbarMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Theme.spacing.md)
                .padding(top = 32.dp)
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