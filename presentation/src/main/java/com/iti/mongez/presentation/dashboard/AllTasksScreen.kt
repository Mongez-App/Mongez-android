package com.iti.mongez.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.chip.AppChip
import com.iti.mongez.designsystem.screens.dashboard.AppTaskCard
import com.iti.mongez.designsystem.screens.dashboard.TaskPriority
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTasksScreen(
    tasks: List<TaskItem>,
    onNavigateBack: () -> Unit,
    onNavigateToStudyRoom: (String, String, String, Int) -> Unit,
) {
    var selectedFilterIndex by remember { mutableStateOf(0) }
    val filters = listOf("All", "Pending", "Completed", "High", "Medium", "Low")

    val filteredTasks = remember(tasks, selectedFilterIndex) {
        when (selectedFilterIndex) {
            1 -> tasks.filter { !it.isCompleted }
            2 -> tasks.filter { it.isCompleted }
            3 -> tasks.filter { it.priority == TaskPriority.HIGH }
            4 -> tasks.filter { it.priority == TaskPriority.MEDIUM }
            5 -> tasks.filter { it.priority == TaskPriority.LOW }
            else -> tasks
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.dashboard_todays_tasks),
                        style = Theme.typography.title.large,
                        fontWeight = FontWeight.Bold,
                        color = Theme.colorScheme.text.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back",
                            tint = Theme.colorScheme.text.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Theme.colorScheme.surface.background
                )
            )
        },
        containerColor = Theme.colorScheme.surface.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(Theme.spacing.sm))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.sm),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Theme.spacing.xl)
            ) {
                items(filters.size) { index ->
                    AppChip(
                        label = filters[index],
                        selected = selectedFilterIndex == index,
                        onSelectedChange = {
                            selectedFilterIndex = index
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(Theme.spacing.md))

            if (filteredTasks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_no_tasks),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(135.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.no_tasks_for_today),
                        style = Theme.typography.body.medium,
                        color = Theme.colorScheme.text.secondary
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = Theme.spacing.xl,
                        vertical = Theme.spacing.sm
                    ),
                    verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        AppTaskCard(
                            title = task.title,
                            duration = task.duration,
                            priority = task.priority,
                            isCompleted = task.isCompleted,
                            onClick = {
                                onNavigateToStudyRoom(task.id, task.title, task.courseId, task.durationMinutes)
                            }
                        )
                    }
                }
            }
        }
    }
}