package com.iti.mongez.presentation.courses.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.tabs.AppSegmentedTabs
import com.iti.mongez.designsystem.components.tabs.SegmentedTabItem
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.components.upload.AppUploadedFileCard
import com.iti.mongez.designsystem.screens.courses.AppUploadBox
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.utils.getFileInfo
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseSheetContent(
    isLoading: Boolean,
    onAddCourse: (name: String, code: String, imageUrl: String, startDate: String, examDate: String, materials: List<Uri>, isOnlineCourse: Boolean, materialUrl: String?) -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    // MOVED HERE: Now the entire screen can access this variable
    val isOnlineCourse = selectedTab == 0

    var courseName by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var courseUrl by remember { mutableStateOf("") }

    var startDateUi by remember { mutableStateOf("") }
    var examDateUi by remember { mutableStateOf("") }
    var startDateIso by remember { mutableStateOf("") }
    var examDateIso by remember { mutableStateOf("") }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showExamDatePicker by remember { mutableStateOf(false) }

    val todayUtc = remember {
        LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    val startDatePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= todayUtc
            }
        }
    )

    val examDatePickerState = rememberDatePickerState(
        selectableDates = remember(startDatePickerState.selectedDateMillis) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val minDate = startDatePickerState.selectedDateMillis ?: todayUtc
                    return utcTimeMillis >= minDate
                }
            }
        }
    )

    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        selectedFiles = (selectedFiles + uris).distinct()
    }

    val tabs = listOf(
        SegmentedTabItem(
            title = stringResource(R.string.tab_online_course),
            iconPainter = painterResource(id = R.drawable.link_icon)
        ),
        SegmentedTabItem(
            title = stringResource(R.string.tab_upload_material),
            iconPainter = painterResource(id = R.drawable.folder_icon)
        )
    )
    var coverImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            coverImageUri = uri
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg)
        ) {
            AppSegmentedTabs(
                items = tabs,
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            AppTextField(
                value = courseName,
                onValueChange = { courseName = it },
                label = stringResource(R.string.label_course_name),
                placeholder = stringResource(R.string.hint_course_name),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )

            if (isOnlineCourse) {
                AppTextField(
                    value = courseUrl,
                    onValueChange = { courseUrl = it },
                    label = stringResource(R.string.label_course_url),
                    placeholder = "https://..."
                )
            }

            AppTextField(
                value = courseCode,
                onValueChange = { courseCode = it },
                label = stringResource(R.string.label_course_code),
                placeholder = stringResource(R.string.hint_course_code)
            )

            if (!isOnlineCourse) {
                if (coverImageUri == null) {
                    AppUploadBox(
                        title = stringResource(R.string.cover_image),
                        primaryText = stringResource(R.string.upload_cover_image),
                        secondaryText = stringResource(R.string.cover_image_hint),
                        onClick = {
                            imagePickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        iconContent = {
                            Text(text = "🖼️", fontSize = 24.sp)
                        }
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.sm)
                    ) {
                        Text(
                            text = stringResource(R.string.cover_image),
                            color = Theme.colorScheme.text.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        AsyncImage(
                            model = coverImageUri,
                            contentDescription = stringResource(R.string.selected_cover_image),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(172.dp)
                                .clip(RoundedCornerShape(Theme.radius.lg))
                                .clickable {
                                    imagePickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                        )
                    }
                }
            }

            if (selectedTab == 1) {
                AppUploadBox(
                    title = stringResource(R.string.course_material_title),
                    primaryText = stringResource(R.string.upload_course_material),
                    secondaryText = stringResource(R.string.upload_course_material_hint),
                    onClick = { filePickerLauncher.launch("*/*") },
                    iconContent = { Text("📚") }
                )

                if (selectedFiles.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Theme.spacing.sm),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        selectedFiles.forEach { uri ->
                            val fileInfo = uri.getFileInfo(context)
                            AppUploadedFileCard(
                                fileName = fileInfo.name,
                                fileSize = fileInfo.sizeFormatted,
                                fileExtension = fileInfo.extension,
                                onRemoveClick = { selectedFiles = selectedFiles - uri }
                            )
                        }
                    }
                }
            }

            AppTextField(
                value = startDateUi,
                onValueChange = {},
                label = stringResource(R.string.label_start_date),
                placeholder = stringResource(R.string.hint_start_date),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showStartDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.select_start_date),
                            tint = Theme.colorScheme.input.icon
                        )
                    }
                }
            )

            AppTextField(
                value = examDateUi,
                onValueChange = {},
                label = stringResource(R.string.label_exam_date),
                placeholder = stringResource(R.string.hint_exam_date),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showExamDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.select_exam_date),
                            tint = Theme.colorScheme.input.icon
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        AppButton(
            text = stringResource(R.string.button_add_course),
            onClick = {
                val mockedImageUrl = if (coverImageUri != null && !isOnlineCourse) {
                    "https://www.dreamstime.com/photos-images/course-word.html"
                } else {
                    ""
                }
                onAddCourse(
                    courseName,
                    courseCode,
                    mockedImageUrl,
                    startDateIso,
                    examDateIso,
                    if (!isOnlineCourse) selectedFiles else emptyList(),
                    isOnlineCourse,
                    if (isOnlineCourse) courseUrl else null
                )
            },
            variant = AppButtonVariant.Primary,
            isLoading = isLoading
        )

        if (showStartDatePicker || showExamDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showStartDatePicker = false
                    showExamDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val activeState = if (showStartDatePicker) startDatePickerState else examDatePickerState
                            activeState.selectedDateMillis?.let { millis ->
                                val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                                val formattedUi = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                val formattedIso = Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)

                                if (showStartDatePicker) {
                                    startDateUi = formattedUi
                                    startDateIso = formattedIso
                                } else {
                                    examDateUi = formattedUi
                                    examDateIso = formattedIso
                                }
                            }
                            showStartDatePicker = false
                            showExamDatePicker = false
                        }
                    ) {
                        Text(stringResource(R.string.action_ok), color = Theme.colorScheme.brand.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showStartDatePicker = false
                        showExamDatePicker = false
                    }) {
                        Text(stringResource(R.string.action_cancel), color = Theme.colorScheme.text.secondary)
                    }
                }
            ) {
                DatePicker(
                    state = if (showStartDatePicker) startDatePickerState else examDatePickerState,
                    colors = DatePickerDefaults.colors(
                        containerColor = Theme.colorScheme.surface.surface,
                        titleContentColor = Theme.colorScheme.text.primary,
                        headlineContentColor = Theme.colorScheme.brand.primary,
                        selectedDayContainerColor = Theme.colorScheme.brand.primary,
                        selectedDayContentColor = Theme.colorScheme.button.primaryContent,
                        todayContentColor = Theme.colorScheme.brand.primary,
                        todayDateBorderColor = Theme.colorScheme.brand.primary
                    )
                )
            }
        }
    }
}