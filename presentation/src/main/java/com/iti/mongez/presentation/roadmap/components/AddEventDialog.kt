package com.iti.mongez.presentation.roadmap.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import androidx.compose.ui.res.stringResource
import com.iti.mongez.presentation.roadmap.uiState.CourseUiModel
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.components.dialog.AppTimePickerDialog
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

enum class AddEventStep {
    TYPE, COURSE, DETAILS
}

@Composable
fun AddEventDialog(
    availableCourses: List<CourseUiModel>,
    onDismiss: () -> Unit,
    onEventCreated: (type: String, courseId: String, name: String, date: String, time: String) -> Unit
) {
    var currentStep by remember { mutableStateOf(AddEventStep.TYPE) }

    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedCourseId by remember { mutableStateOf<String?>(null) }
    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(Theme.radius.dialog),
            color = Theme.colorScheme.surface.background,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.spacing.xl)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = Theme.spacing.xl),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.add_new_event_title),
                        style = Theme.typography.headline.small,
                        color = Theme.colorScheme.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(com.iti.mongez.designsystem.R.string.cd_close),
                            tint = Theme.colorScheme.text.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Theme.spacing.lg))

                // Progress Indicator
                WizardProgressIndicator(currentStep = currentStep)

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                // Animated Content for Steps
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState.ordinal > initialState.ordinal) {
                            slideInHorizontally(animationSpec = tween(300)) { width -> width } + fadeIn() togetherWith
                                    slideOutHorizontally(animationSpec = tween(300)) { width -> -width } + fadeOut()
                        } else {
                            slideInHorizontally(animationSpec = tween(300)) { width -> -width } + fadeIn() togetherWith
                                    slideOutHorizontally(animationSpec = tween(300)) { width -> width } + fadeOut()
                        }
                    },
                    label = "Wizard Transition"
                ) { step ->
                    when (step) {
                        AddEventStep.TYPE -> {
                            StepOneType(
                                selectedType = selectedType,
                                onTypeSelected = { selectedType = it },
                                onContinue = { currentStep = AddEventStep.COURSE }
                            )
                        }
                        AddEventStep.COURSE -> {
                            StepTwoCourse(
                                availableCourses = availableCourses,
                                selectedCourseId = selectedCourseId,
                                onCourseSelected = { selectedCourseId = it },
                                onBack = { currentStep = AddEventStep.TYPE },
                                onContinue = { currentStep = AddEventStep.DETAILS }
                            )
                        }
                        AddEventStep.DETAILS -> {
                            StepThreeDetails(
                                eventName = eventName,
                                onNameChange = { eventName = it },
                                eventDate = eventDate,
                                onDateChange = { eventDate = it },
                                eventTime = eventTime,
                                onTimeChange = { eventTime = it },
                                onBack = { currentStep = AddEventStep.COURSE },
                                onCreate = {
                                    selectedType?.let { type ->
                                        selectedCourseId?.let { courseId ->
                                            onEventCreated(type, courseId, eventName, eventDate, eventTime)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WizardProgressIndicator(currentStep: AddEventStep) {
    val steps = AddEventStep.entries.toTypedArray()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, step ->
            val isCompleted = index < currentStep.ordinal
            val isCurrent = index == currentStep.ordinal

            // Node
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> Theme.colorScheme.brand.primary
                            isCurrent -> Theme.colorScheme.brand.primary
                            else -> Theme.colorScheme.surface.surfaceVariant
                        }
                    )
                    .border(
                        width = 2.dp,
                        color = if (isCurrent) Theme.colorScheme.brand.primaryContainer else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            // Connecting Line
            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .padding(horizontal = 8.dp)
                        .background(
                            if (isCompleted) Theme.colorScheme.brand.primary
                            else Theme.colorScheme.surface.surfaceVariant
                        )
                )
            }
        }
    }

    // Labels
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.add_event_step_type), style = Theme.typography.label.small, color = if (currentStep.ordinal >= 0) Theme.colorScheme.brand.primary else Theme.colorScheme.text.secondary)
        Text(stringResource(R.string.add_event_step_course), style = Theme.typography.label.small, color = if (currentStep.ordinal >= 1) Theme.colorScheme.brand.primary else Theme.colorScheme.text.secondary)
        Text(stringResource(R.string.add_event_step_details), style = Theme.typography.label.small, color = if (currentStep.ordinal >= 2) Theme.colorScheme.brand.primary else Theme.colorScheme.text.secondary)
    }
}

@Composable
private fun StepOneType(
    selectedType: String?,
    onTypeSelected: (String) -> Unit,
    onContinue: () -> Unit
) {
    val eventColors = Theme.colorScheme.events
    val eventTypes = listOf(
        EventTypeUiModel("assignment", stringResource(R.string.event_assignment_title), stringResource(R.string.event_assignment_desc), Icons.AutoMirrored.Rounded.Assignment, eventColors.assignmentContainer, eventColors.assignmentIcon),
        EventTypeUiModel("quiz", stringResource(R.string.event_quiz_title), stringResource(R.string.event_quiz_desc), Icons.Rounded.Quiz, eventColors.quizContainer, eventColors.quizIcon),
        EventTypeUiModel("midterm", stringResource(R.string.event_midterm_title), stringResource(R.string.event_midterm_desc), Icons.Rounded.Description, eventColors.examContainer, eventColors.examIcon),
        EventTypeUiModel("exam", stringResource(R.string.event_exam_title), stringResource(R.string.event_exam_desc), Icons.Rounded.School, eventColors.examContainer, eventColors.examIcon),
        EventTypeUiModel("project", stringResource(R.string.event_project_title), stringResource(R.string.event_project_desc), Icons.Rounded.GridView, eventColors.projectContainer, eventColors.projectIcon)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.add_event_choose_type),
            style = Theme.typography.title.large,
            fontWeight = FontWeight.SemiBold,
            color = Theme.colorScheme.text.primary
        )
        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            items(eventTypes) { type ->
                val isSelected = selectedType == type.id
                val backgroundColor = if (isSelected) Theme.colorScheme.brand.primaryContainer else Theme.colorScheme.surface.surfaceLow
                val borderColor = if (isSelected) Theme.colorScheme.brand.primary else Color.Transparent

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    border = BorderStroke(2.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTypeSelected(type.id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Theme.spacing.lg),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(type.containerColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = type.icon,
                                contentDescription = null,
                                tint = type.iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(Theme.spacing.lg))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = type.title,
                                style = Theme.typography.title.medium,
                                color = Theme.colorScheme.text.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = type.description,
                                style = Theme.typography.body.small,
                                color = Theme.colorScheme.text.secondary
                            )
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = stringResource(com.iti.mongez.designsystem.R.string.cd_completed),
                                tint = Theme.colorScheme.brand.primary
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = onContinue,
            enabled = selectedType != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Theme.spacing.lg),
            colors = ButtonDefaults.buttonColors(containerColor = Theme.colorScheme.brand.primary),
            shape = RoundedCornerShape(100)
        ) {
            Text(stringResource(R.string.add_event_continue), modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepTwoCourse(
    availableCourses: List<CourseUiModel>,
    selectedCourseId: String?,
    onCourseSelected: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.add_event_choose_course),
            style = Theme.typography.title.large,
            fontWeight = FontWeight.SemiBold,
            color = Theme.colorScheme.text.primary
        )
        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            availableCourses.forEach { course ->
                val isSelected = selectedCourseId == course.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onCourseSelected(course.id) },
                    label = {
                        Text(
                            text = course.name,
                            color = if (isSelected) Color.White else Theme.colorScheme.text.primary,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Theme.colorScheme.brand.primary,
                        containerColor = Theme.colorScheme.surface.surfaceLow
                    ),
                    border = if (isSelected) null else FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = false,
                        borderColor = Theme.colorScheme.border.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Theme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.add_event_back), color = Theme.colorScheme.text.secondary)
            }
            Button(
                onClick = onContinue,
                enabled = selectedCourseId != null,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Theme.colorScheme.brand.primary),
                shape = RoundedCornerShape(100)
            ) {
                Text(stringResource(R.string.add_event_continue))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepThreeDetails(
    eventName: String,
    onNameChange: (String) -> Unit,
    eventDate: String,
    onDateChange: (String) -> Unit,
    eventTime: String,
    onTimeChange: (String) -> Unit,
    onBack: () -> Unit,
    onCreate: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val todayUtc = LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
                return utcTimeMillis >= todayUtc
            }
        }
    )
    val timePickerState = rememberTimePickerState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.add_event_details),
            style = Theme.typography.title.large,
            fontWeight = FontWeight.SemiBold,
            color = Theme.colorScheme.text.primary
        )
        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            AppTextField(
                value = eventName,
                onValueChange = onNameChange,
                label = stringResource(R.string.add_event_name_label),
                placeholder = stringResource(R.string.add_event_name_hint),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                )
            )

            AppTextField(
                value = eventDate,
                onValueChange = {},
                label = stringResource(R.string.add_event_date_label),
                placeholder = stringResource(R.string.add_event_date_hint),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = stringResource(R.string.add_event_date_hint),
                            tint = Theme.colorScheme.input.icon
                        )
                    }
                }
            )

            AppTextField(
                value = eventTime,
                onValueChange = {},
                label = stringResource(R.string.add_event_time_label),
                placeholder = stringResource(R.string.add_event_time_hint),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = stringResource(R.string.add_event_time_hint),
                            tint = Theme.colorScheme.input.icon
                        )
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Theme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            Button(
                onClick = onCreate,
                enabled = eventName.isNotBlank() && eventDate.isNotBlank() && eventTime.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Theme.colorScheme.brand.primary),
                shape = RoundedCornerShape(100)
            ) {
                Text(stringResource(R.string.add_event_create), modifier = Modifier.padding(vertical = 8.dp))
            }
            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_event_back), color = Theme.colorScheme.text.secondary)
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            onDateChange(localDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(com.iti.mongez.designsystem.R.string.action_ok), color = Theme.colorScheme.brand.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(com.iti.mongez.designsystem.R.string.action_cancel), color = Theme.colorScheme.text.secondary)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Theme.colorScheme.brand.primary,
                    todayDateBorderColor = Theme.colorScheme.brand.primary,
                    todayContentColor = Theme.colorScheme.brand.primary
                )
            )
        }
    }

    if (showTimePicker) {
        AppTimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onConfirm = { state ->
                val time = LocalTime.of(state.hour, state.minute)
                onTimeChange(time.format(DateTimeFormatter.ofPattern("hh:mm a")))
                showTimePicker = false
            },
            state = timePickerState
        )
    }
}

private data class EventTypeUiModel(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val containerColor: Color,
    val iconColor: Color
)