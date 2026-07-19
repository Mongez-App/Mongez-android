package com.iti.mongez.presentation.courses

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            title = "Online Course",
            iconPainter = painterResource(id = R.drawable.link_icon)
        ),
        SegmentedTabItem(
            title = "Upload Material",
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
            label = "Course Name",
            placeholder = "e.g. Operating Systems"
        )

        AppTextField(
            value = courseCode,
            onValueChange = { courseCode = it },
            label = "Course Code",
            placeholder = "e.g. CS301"
        )

        if (selectedTab == 1) {
            AppUploadBox(
                title = "Course Material",
                primaryText = "Upload course material",
                secondaryText = "PDF or DOC, up to 10 MB",
                onClick = {},
                iconContent = { Text("📚") }
            )
        }

        AppTextField(
            value = startDateUi,
            onValueChange = {},
            label = "Start Date",
            placeholder = "14/07/2026",
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
            label = "Exam Date",
            placeholder = "15/08/2026",
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showExamDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select exam date",
                        tint = Theme.colorScheme.input.icon
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(Theme.spacing.sm))

        AppButton(
            text = "Add Course",
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
                    Text("OK", color = Theme.colorScheme.brand.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showStartDatePicker = false
                    showExamDatePicker = false
                }) {
                    Text("Cancel", color = Theme.colorScheme.text.secondary)
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