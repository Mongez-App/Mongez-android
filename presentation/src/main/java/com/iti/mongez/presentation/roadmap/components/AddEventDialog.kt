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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import androidx.compose.ui.res.stringResource
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.components.dialog.AppTimePickerDialog
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class AddEventStep {
    TYPE, COURSE, DETAILS
}

@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onEventCreated: (type: String, course: String, name: String, date: String, time: String, notes: String) -> Unit
) {
    var currentStep by remember { mutableStateOf(AddEventStep.TYPE) }

    var selectedType by remember { mutableStateOf<String?>(null) }
    var selectedCourse by remember { mutableStateOf<String?>(null) }
    var eventName by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventNotes by remember { mutableStateOf("") }

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
                        text = "Add New Event",
                        style = Theme.typography.headline.small,
                        color = Theme.colorScheme.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
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
                                selectedCourse = selectedCourse,
                                onCourseSelected = { selectedCourse = it },
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
                                eventNotes = eventNotes,
                                onNotesChange = { eventNotes = it },
                                onBack = { currentStep = AddEventStep.COURSE },
                                onCreate = {
                                    selectedType?.let { type ->
                                        selectedCourse?.let { course ->
                                            onEventCreated(type, course, eventName, eventDate, eventTime, eventNotes)
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
        Text("Type", style = Theme.typography.label.small, color = if (currentStep.ordinal >= 0) Theme.colorScheme.brand.primary else Theme.colorScheme.text.secondary)
        Text("Course", style = Theme.typography.label.small, color = if (currentStep.ordinal >= 1) Theme.colorScheme.brand.primary else Theme.colorScheme.text.secondary)
        Text("Details", style = Theme.typography.label.small, color = if (currentStep.ordinal >= 2) Theme.colorScheme.brand.primary else Theme.colorScheme.text.secondary)
    }
}

@Composable
private fun StepOneType(
    selectedType: String?,
    onTypeSelected: (String) -> Unit,
    onContinue: () -> Unit
) {
    val eventTypes = listOf(
        EventTypeUiModel("study", "Study Session", "Focused time for reviewing materials", Icons.Rounded.MenuBook, Color(0xFFE8DEF8), Color(0xFF6750A4)), // Purple
        EventTypeUiModel("assignment", "Assignment", "Homework, papers, or lab reports", Icons.AutoMirrored.Rounded.Assignment, Color(0xFFFFDBCF), Color(0xFFF96025)), // Orange
        EventTypeUiModel("quiz", "Quiz", "Short assessments and pop quizzes", Icons.Rounded.Quiz, Color(0xFFCCE8E4), Color(0xFF00796B)), // Teal
        EventTypeUiModel("exam", "Exam", "Major tests and midterms/finals", Icons.Rounded.School, Color(0xFFFFDAD6), Color(0xFFBA1A1A)), // Red
        EventTypeUiModel("project", "Project", "Long-term assignments and presentations", Icons.Rounded.GridView, Color(0xFFD3E3FD), Color(0xFF0B57D0)) // Blue
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Choose Event Type",
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
                                contentDescription = "Selected",
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
            Text("Continue", modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepTwoCourse(
    selectedCourse: String?,
    onCourseSelected: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit
) {
    val courses = listOf("Math", "Physics", "Programming", "Chemistry", "English", "Biology", "Database Systems", "Operating Systems")

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Choose Course",
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
            courses.forEach { course ->
                val isSelected = selectedCourse == course
                FilterChip(
                    selected = isSelected,
                    onClick = { onCourseSelected(course) },
                    label = {
                        Text(
                            text = course,
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
                Text("Back", color = Theme.colorScheme.text.secondary)
            }
            Button(
                onClick = onContinue,
                enabled = selectedCourse != null,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Theme.colorScheme.brand.primary),
                shape = RoundedCornerShape(100)
            ) {
                Text("Continue")
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
    eventNotes: String,
    onNotesChange: (String) -> Unit,
    onBack: () -> Unit,
    onCreate: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Event Details",
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
                label = "Event Name",
                placeholder = "Enter event name"
            )

            AppTextField(
                value = eventDate,
                onValueChange = {},
                label = "Date",
                placeholder = "Select date",
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarToday,
                            contentDescription = "Select Date",
                            tint = Theme.colorScheme.input.icon
                        )
                    }
                }
            )

            AppTextField(
                value = eventTime,
                onValueChange = {},
                label = "Time",
                placeholder = "Select time",
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = "Select Time",
                            tint = Theme.colorScheme.input.icon
                        )
                    }
                }
            )

            AppTextField(
                value = eventNotes,
                onValueChange = onNotesChange,
                label = "Optional Notes",
                placeholder = "Add any extra details here...",
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )
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
                Text("Back", color = Theme.colorScheme.text.secondary)
            }
            Button(
                onClick = onCreate,
                enabled = eventName.isNotBlank() && eventDate.isNotBlank() && eventTime.isNotBlank(),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Theme.colorScheme.brand.primary),
                shape = RoundedCornerShape(100)
            ) {
                Text("Create Event")
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
                    Text("OK", color = Theme.colorScheme.brand.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Theme.colorScheme.text.secondary)
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