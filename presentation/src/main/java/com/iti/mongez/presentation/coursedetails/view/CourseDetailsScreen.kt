package com.iti.mongez.presentation.coursedetails.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.button.AppGlowButton
import com.iti.mongez.designsystem.components.chip.AppChip
import com.iti.mongez.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.designsystem.components.menu.AppPopupMenu
import com.iti.mongez.designsystem.components.menu.PopupMenuItem
import com.iti.mongez.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.components.tabs.AppPrimaryTabs
import com.iti.mongez.designsystem.screens.courses.AppDocumentCard
import com.iti.mongez.designsystem.screens.courses.AppTaskCard
import com.iti.mongez.designsystem.screens.courses.CourseProgressCard
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.coursedetails.components.EditCourseSheetContent
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsEffect
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsIntent
import com.iti.mongez.presentation.coursedetails.utils.FilePickerHelper
import com.iti.mongez.presentation.coursedetails.viewmodel.CourseDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    courseId: String,
    viewModel: CourseDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToStudyRoom: (String, String) -> Unit,
    // Added optional parameters with defaults to preserve existing usages across the app
    allowEditing: Boolean = true,
    showUploadMaterial: Boolean = true
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isCourseMenuExpanded by remember { mutableStateOf(false) }
    var isEditBottomSheetOpen by remember { mutableStateOf(false) }

    var topSnackbarMessage by remember { mutableStateOf<String?>(null) }
    var topSnackbarType by remember { mutableStateOf(AppSnackbarType.Info) }

    LaunchedEffect(courseId) {
        viewModel.processIntent(CourseDetailsIntent.LoadCourse(courseId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CourseDetailsEffect.ShowSnackbar -> {
                    topSnackbarMessage = effect.message
                    topSnackbarType = effect.type
                }
                is CourseDetailsEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is CourseDetailsEffect.OpenPdf -> {
                    coroutineScope.launch {
                        com.iti.mongez.presentation.coursedetails.utils.PdfOpener.openPdf(
                            context = context,
                            uriString = effect.uriString,
                            onError = { errorMessage ->
                                topSnackbarMessage = errorMessage
                                topSnackbarType = AppSnackbarType.Error
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(topSnackbarMessage) {
        if (topSnackbarMessage != null) {
            delay(3000L)
            topSnackbarMessage = null
        }
    }

    val filePickerLauncher = FilePickerHelper.rememberFilePickerLauncher(
        context = context,
        coroutineScope = coroutineScope,
        onFileSelected = { fileName, mimeType, fileSize, bytes, uriString ->
            viewModel.uploadFile(fileName, mimeType, fileSize, 1, bytes, deviceFileUri = uriString)
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Theme.colorScheme.surface.background,
            bottomBar = {
                // Conditionally render the upload button based on the flag and course type
                if (showUploadMaterial && state.courseType != "URL_COURSE") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.xl)
                    ) {
                        AppGlowButton(
                            text = stringResource(R.string.upload_course_material),
                            onClick = { filePickerLauncher.launch(arrayOf("application/pdf")) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Top Navigation Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Theme.spacing.md, vertical = Theme.spacing.sm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                viewModel.processIntent(CourseDetailsIntent.ClickBack)
                                onNavigateBack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = stringResource(R.string.back),
                                tint = Theme.colorScheme.brand.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = { isCourseMenuExpanded = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreHoriz,
                                    contentDescription = stringResource(R.string.course_options),
                                    tint = Theme.colorScheme.text.primary
                                )
                            }

                            // Dynamically build menu options based on allowEditing flag
                            val menuItems = mutableListOf<PopupMenuItem>().apply {
                                if (allowEditing) {
                                    add(
                                        PopupMenuItem(
                                            title = stringResource(R.string.edit_course),
                                            icon = Icons.Outlined.Edit,
                                            color = Theme.colorScheme.text.primary,
                                            height = 48.dp,
                                            padding = PaddingValues(
                                                horizontal = Theme.spacing.lg,
                                                vertical = Theme.spacing.md
                                            ),
                                            onClick = {
                                                isCourseMenuExpanded = false
                                                isEditBottomSheetOpen = true
                                            }
                                        )
                                    )
                                }
                                add(
                                    PopupMenuItem(
                                        title = stringResource(R.string.delete_course),
                                        icon = Icons.Outlined.Delete,
                                        color = Theme.colorScheme.state.error,
                                        height = 48.dp,
                                        padding = PaddingValues(
                                            start = Theme.spacing.lg,
                                            end = Theme.spacing.lg,
                                            top = Theme.spacing.lg,
                                            bottom = Theme.spacing.md
                                        ),
                                        onClick = {
                                            isCourseMenuExpanded = false
                                            viewModel.processIntent(CourseDetailsIntent.ShowDeleteDialog)
                                        }
                                    )
                                )
                            }

                            AppPopupMenu(
                                expanded = isCourseMenuExpanded,
                                onDismissRequest = { isCourseMenuExpanded = false },
                                items = menuItems
                            )
                        }
                    }

                    // Screen Title
                    Text(
                        text = state.courseTitle,
                        color = Theme.colorScheme.text.primary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = Theme.spacing.lg,
                            vertical = Theme.spacing.xs
                        )
                    )

                    Spacer(modifier = Modifier.height(Theme.spacing.md))

                    // Custom Tabs
                    val tabTitles = state.tabs.map { stringResource(it) }
                    AppPrimaryTabs(
                        tabs = tabTitles,
                        selectedTabIndex = state.selectedTabIndex,
                        onTabSelected = { index ->
                            viewModel.processIntent(CourseDetailsIntent.SelectTab(index))
                        }
                    )

                    // Scrollable Content Area
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Theme.spacing.lg),
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(Theme.spacing.md))
                        }

                        // TAB 0: MATERIALS
                        if (state.selectedTabIndex == 0) {
                            if (state.materials.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = Theme.spacing.giant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "📁",
                                                style = Theme.typography.headline.large,
                                                modifier = Modifier.padding(bottom = Theme.spacing.sm)
                                            )
                                            Text(
                                                text = if (state.courseType == "URL_COURSE") "This course's material is on the platform" else stringResource(R.string.no_materials_empty_state),
                                                style = Theme.typography.body.large,
                                                color = Theme.colorScheme.text.secondary
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(state.materials, key = { it.id }) { document ->
                                    AppDocumentCard(
                                        title = document.title,
                                        pageCount = document.pageCount,
                                        fileSize = document.fileSize,
                                        fileExtension = document.fileExtension,
                                        onClick = {
                                            viewModel.processIntent(CourseDetailsIntent.ClickDocument(document.id))
                                        },
                                        onDeleteClick = {
                                            viewModel.processIntent(CourseDetailsIntent.DeleteDocument(document.id))
                                        }
                                    )
                                }
                            }
                        }

                        // TAB 1: TASKS
                        else if (state.selectedTabIndex == 1) {
                            item {
                                CourseProgressCard(
                                    completedTasks = state.completedTasks,
                                    totalTasks = state.totalTasks,
                                    percentage = state.progressPercentage
                                )
                            }

                            item { Spacer(modifier = Modifier.height(Theme.spacing.lg)) }

                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing.sm),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(state.taskFilters.size) { index ->
                                        val filterRes = state.taskFilters[index]
                                        AppChip(
                                            label = stringResource(filterRes),
                                            selected = state.selectedTaskFilterIndex == index,
                                            onSelectedChange = {
                                                viewModel.processIntent(
                                                    CourseDetailsIntent.SelectTaskFilter(index)
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            val todayTasks = state.tasks.filter { it.isToday }
                            if (todayTasks.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(Theme.spacing.lg))
                                    Text(
                                        text = stringResource(R.string.today_tasks),
                                        style = Theme.typography.title.medium.copy(fontWeight = FontWeight.Bold),
                                        color = Theme.colorScheme.text.primary,
                                        modifier = Modifier.padding(bottom = Theme.spacing.sm)
                                    )
                                }
                                items(todayTasks, key = { it.id }) { task ->
                                    AppTaskCard(
                                        title = task.title,
                                        duration = task.duration,
                                        priority = task.priority,
                                        isCompleted = task.isCompleted,
                                        onClick = {
                                            onNavigateToStudyRoom(task.id, task.title)
                                        }
                                    )
                                }
                            }

                            val upcomingTasks = state.tasks.filter { !it.isToday }
                            if (upcomingTasks.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(Theme.spacing.lg))
                                    Text(
                                        text = stringResource(R.string.upcoming_tasks),
                                        style = Theme.typography.title.medium.copy(fontWeight = FontWeight.Bold),
                                        color = Theme.colorScheme.text.primary,
                                        modifier = Modifier.padding(bottom = Theme.spacing.sm)
                                    )
                                }
                                items(upcomingTasks) { task ->
                                    AppTaskCard(
                                        title = task.title,
                                        duration = task.duration,
                                        priority = task.priority,
                                        isCompleted = task.isCompleted,
                                        onClick = {
                                            onNavigateToStudyRoom(task.id, task.title)
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }

                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Theme.colorScheme.brand.primary
                        )
                    }
                }
            }
        }

        if (state.isDeleteDialogVisible) {
            AppConfirmationDialog(
                title = stringResource(R.string.delete_course_title),
                description = stringResource(R.string.delete_course_dialog_description),
                primaryActionText = stringResource(R.string.action_delete),
                onPrimaryAction = {
                    viewModel.processIntent(CourseDetailsIntent.DeleteCourse)
                },
                onDismiss = {
                    viewModel.processIntent(CourseDetailsIntent.DismissDeleteDialog)
                },
                secondaryActionText = stringResource(R.string.action_cancel),
                onSecondaryAction = {
                    viewModel.processIntent(CourseDetailsIntent.DismissDeleteDialog)
                }
            )
        }

        // Edit Course Bottom Sheet (Guarded by allowEditing)
        if (allowEditing && isEditBottomSheetOpen) {
            AppBottomSheet(
                onDismiss = { isEditBottomSheetOpen = false },
                title = stringResource(R.string.edit_course)
            ) {
                EditCourseSheetContent(
                    initialName = state.courseTitle,
                    initialImageUrl = state.imageUrl,
                    courseCode = state.courseCode,
                    startDateIso = state.startDate,
                    examDateIso = state.examDate,
                    courseUrl = state.materialUrl ?: "",
                    isOnline = state.courseType == "URL_COURSE",
                    isLoading = state.isLoading,
                    onCancel = { isEditBottomSheetOpen = false },
                    onSave = { name, imageUrl ->
                        isEditBottomSheetOpen = false
                        viewModel.processIntent(CourseDetailsIntent.UpdateCourse(name = name, imageUrl = imageUrl))
                    }
                )
            }
        }

        // Animated Snackbar
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