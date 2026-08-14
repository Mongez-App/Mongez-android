package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.components.sheet.AppBottomSheet
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState
import com.iti.mongez.presentation.roadmap.uiState.CourseUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    initialState: RoadmapFilterState,
    availableCourses: List<CourseUiModel>,
    availableEventTypes: List<String>,
    onDismiss: () -> Unit,
    onApply: (RoadmapFilterState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var localState by remember { mutableStateOf(initialState) }

    AppBottomSheet(
        onDismiss = onDismiss,
        title = stringResource(R.string.filter_roadmap_title),
        sheetState = sheetState
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
    availableCourses: List<CourseUiModel>,
    availableEventTypes: List<String>,
    onApply: (RoadmapFilterState) -> Unit,
    onStateChange: (RoadmapFilterState) -> Unit,
    onReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxHeight(0.85f)) {
        HorizontalDivider(
            Modifier.padding(vertical = Theme.spacing.sm),
            DividerDefaults.Thickness,
            color = Theme.colorScheme.border.primary
        )

        Column(
            modifier = Modifier
                .weight(1f)
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
                availableCourses = listOf(
                    CourseUiModel("1", "Algorithms"),
                    CourseUiModel("2", "Database Systems"),
                    CourseUiModel("3", "Networks"),
                    CourseUiModel("4", "Operating Systems")
                ),
                availableEventTypes = listOf("assignment", "quiz", "midterm", "exam", "project"),
                onApply = {},
                onStateChange = {},
                onReset = {}
            )
        }
    }
}