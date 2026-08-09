package com.iti.mongez.feature.coursedetails.view

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.button.AppGlowButton
import com.iti.mongez.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.designsystem.components.menu.AppPopupMenu
import com.iti.mongez.designsystem.components.menu.PopupMenuItem
import com.iti.mongez.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.components.tabs.AppPrimaryTabs
import com.iti.mongez.designsystem.components.chip.AppChip
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.screens.courses.AppDocumentCard
import com.iti.mongez.designsystem.screens.courses.AppTaskCard
import com.iti.mongez.designsystem.screens.courses.CourseProgressCard
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.feature.coursedetails.viewmodel.CourseDetailsViewModel
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsEffect
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    courseId: String,
    viewModel: CourseDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToStudyRoom: (String, String) -> Unit
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
            }
        }
    }

    LaunchedEffect(topSnackbarMessage) {
        if (topSnackbarMessage != null) {
            delay(3000L)
            topSnackbarMessage = null
        }
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { selectedUri ->
            coroutineScope.launch(Dispatchers.IO) {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(selectedUri) ?: "application/pdf"

                var fileName = "document.pdf"
                var fileSize = 0L

                contentResolver.query(selectedUri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (cursor.moveToFirst()) {
                        fileName = cursor.getString(nameIndex)
                        fileSize = cursor.getLong(sizeIndex)
                    }
                }

                val inputStream = contentResolver.openInputStream(selectedUri)
                val bytes = inputStream?.readBytes() ?: ByteArray(0)
                inputStream?.close()

                if (bytes.isNotEmpty()) {
                    viewModel.uploadFile(fileName, mimeType, fileSize, 1, bytes)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Theme.colorScheme.surface.background,
            bottomBar = {
                // Ensure state.courseType exists in your UI State
                if (state.courseType != "URL_COURSE") {
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

                            AppPopupMenu(
                                expanded = isCourseMenuExpanded,
                                onDismissRequest = { isCourseMenuExpanded = false },
                                items = listOf(
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
                                    ),
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
                                            // Problem 2 Fix: Dynamic empty state messaging
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
                                items(upcomingTasks, key = { it.id }) { task ->
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

        // Edit Course Bottom Sheet
        if (isEditBottomSheetOpen) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseSheetContent(
    initialName: String,
    initialImageUrl: String,
    courseCode: String,
    startDateIso: String,
    examDateIso: String,
    courseUrl: String,
    isOnline: Boolean,
    isLoading: Boolean,
    onCancel: () -> Unit,
    onSave: (name: String, imageUrl: String) -> Unit
) {
    var courseName by remember { mutableStateOf(initialName) }
    var imageUrl by remember { mutableStateOf(initialImageUrl) }

    fun formatIsoToUi(isoString: String): String {
        return try {
            val instant = Instant.parse(isoString)
            instant.atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            isoString
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg)
    ) {
        // Editable fields
        AppTextField(
            value = courseName,
            onValueChange = { courseName = it },
            label = stringResource(R.string.label_course_name),
            placeholder = stringResource(R.string.hint_course_name),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            )
        )

        AppTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = "Course Image URL",
            placeholder = "https://..."
        )

        // Read-only fields
        if (isOnline) {
            AppTextField(
                value = courseUrl,
                onValueChange = {},
                label = stringResource(R.string.label_course_url),
                readOnly = true,
                placeholder = "https://..."
            )
        }

        AppTextField(
            value = courseCode,
            onValueChange = {},
            label = stringResource(R.string.label_course_code),
            readOnly = true
        )

        AppTextField(
            value = formatIsoToUi(startDateIso),
            onValueChange = {},
            label = stringResource(R.string.label_start_date),
            readOnly = true
        )

        AppTextField(
            value = formatIsoToUi(examDateIso),
            onValueChange = {},
            label = stringResource(R.string.label_exam_date),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.action_cancel),
                    color = Theme.colorScheme.text.secondary
                )
            }

            AppButton(
                text = "Save Changes",
                onClick = { onSave(courseName, imageUrl) },
                modifier = Modifier.weight(1f),
                variant = AppButtonVariant.Primary,
                isLoading = isLoading
            )
        }
    }
}