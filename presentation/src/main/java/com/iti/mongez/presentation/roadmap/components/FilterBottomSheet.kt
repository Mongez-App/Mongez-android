package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    initialState: RoadmapFilterState,
    availableCourses: List<String>,
    availableEventTypes: List<String>,
    onDismiss: () -> Unit,
    onApply: (RoadmapFilterState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var localState by remember { mutableStateOf(initialState) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Theme.colorScheme.surface.background,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        FilterBottomSheetContent(
            localState = localState,
            availableCourses = availableCourses,
            availableEventTypes = availableEventTypes,
            onApply = onApply,
            onStateChange = { localState = it },
            onReset = { localState = RoadmapFilterState() }
        )
    }
}

@Composable
fun FilterBottomSheetContent(
    localState: RoadmapFilterState,
    availableCourses: List<String>,
    availableEventTypes: List<String>,
    onApply: (RoadmapFilterState) -> Unit,
    onStateChange: (RoadmapFilterState) -> Unit,
    onReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxHeight(0.9f)) {
        Text(
            text = "Filter Roadmap",
            style = Theme.typography.title.large,
            fontWeight = FontWeight.Bold,
            color = Theme.colorScheme.text.primary,
            modifier = Modifier.padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.sm)
        )

        HorizontalDivider(
            Modifier,
            DividerDefaults.Thickness,
            color = Theme.colorScheme.border.primary
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Theme.spacing.lg)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Theme.spacing.lg))

            DateRangeSection(
                state = localState,
                onStateChange = onStateChange
            )

            Spacer(modifier = Modifier.height(Theme.spacing.xl))
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = Theme.colorScheme.border.secondary
            )
            Spacer(modifier = Modifier.height(Theme.spacing.lg))

            CourseFilterSection(
                availableCourses = availableCourses,
                state = localState,
                onStateChange = onStateChange
            )

            Spacer(modifier = Modifier.height(Theme.spacing.xl))
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = Theme.colorScheme.border.secondary
            )
            Spacer(modifier = Modifier.height(Theme.spacing.lg))

            EventTypeSection(
                availableEventTypes = availableEventTypes,
                state = localState,
                onStateChange = onStateChange
            )

            Spacer(modifier = Modifier.height(Theme.spacing.xxl))
        }

        BottomActions(
            activeCount = localState.activeFilterCount,
            onReset = onReset,
            onApply = { onApply(localState) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterBottomSheetPreview() {
    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            FilterBottomSheetContent(
                localState = RoadmapFilterState(),
                availableCourses = listOf("Algorithms", "Database Systems", "Networks", "Operating Systems"),
                availableEventTypes = listOf("Study", "Assignment", "Quiz", "Exam"),
                onApply = {},
                onStateChange = {},
                onReset = {}
            )
        }
    }
}