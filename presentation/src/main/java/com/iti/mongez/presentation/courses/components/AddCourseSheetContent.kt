package com.iti.mongez.presentation.courses.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.tabs.AppSegmentedTabs
import com.iti.mongez.designsystem.components.tabs.SegmentedTabItem
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.screens.courses.AppUploadBox
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseSheetContent(
    isLoading: Boolean,
    onAddCourse: (name: String, code: String, startDate: String, examDate: String, hasMaterials: Boolean) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var courseName by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }

    // UI Formatted Dates
    var startDateUi by remember { mutableStateOf("") }
    var examDateUi by remember { mutableStateOf("") }

    // ISO-8601 Backend Dates
    var startDateIso by remember { mutableStateOf("") }
    var examDateIso by remember { mutableStateOf("") }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showExamDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

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

    Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg)) {
        AppSegmentedTabs(
            items = tabs,
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        AppTextField(
            value = courseName,
            onValueChange = { courseName = it },
            label = stringResource(R.string.label_course_name),
            placeholder = stringResource(R.string.hint_course_name)
        )

        AppTextField(
            value = courseCode,
            onValueChange = { courseCode = it },
            label = stringResource(R.string.label_course_code),
            placeholder = stringResource(R.string.hint_course_code)
        )

        if (selectedTab == 1) {
            AppUploadBox(
                title = stringResource(R.string.course_material_title),
                primaryText = stringResource(R.string.upload_course_material),
                secondaryText = stringResource(R.string.upload_course_material_hint),
                onClick = {},
                iconContent = { Text("📚") }
            )
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
                        contentDescription = "Select start date",
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
                        contentDescription = stringResource(R.string.cd_select_exam_date),
                        tint = Theme.colorScheme.input.icon
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(Theme.spacing.sm))

        AppButton(
            text = stringResource(R.string.button_add_course),
            onClick = {
                onAddCourse(
                    courseName,
                    courseCode,
                    startDateIso,
                    examDateIso,
                    selectedTab == 1 // hasMaterials true if Upload Material tab is selected
                )
            },
            variant = AppButtonVariant.Primary,
            isLoading = isLoading
        )
    }

    if (showStartDatePicker || showExamDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showStartDatePicker = false
                showExamDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
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
                state = datePickerState,
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