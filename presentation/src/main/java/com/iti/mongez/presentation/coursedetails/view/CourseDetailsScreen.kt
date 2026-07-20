package com.iti.mongez.feature.coursedetails.view

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.button.AppGlowButton
import com.iti.mongez.designsystem.components.menu.AppPopupMenu
import com.iti.mongez.designsystem.components.menu.PopupMenuItem
import com.iti.mongez.designsystem.components.tabs.AppPrimaryTabs
import com.iti.mongez.designsystem.components.chip.AppChip
import com.iti.mongez.designsystem.screens.courses.AppDocumentCard
import com.iti.mongez.designsystem.screens.courses.AppTaskCard
import com.iti.mongez.designsystem.screens.courses.CourseProgressCard
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.feature.coursedetails.viewmodel.CourseDetailsViewModel
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsIntent

@Composable
fun CourseDetailsScreen(
    viewModel: CourseDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // State for Top Bar Course Menu (Edit & Delete Course)
    var isCourseMenuExpanded by remember { mutableStateOf(false) }

    // State for tracking which document card's menu is open (Open for extension)
    var expandedDocumentId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Theme.colorScheme.surface.background,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.xl)
            ) {
                AppGlowButton(
                    text = stringResource(R.string.upload_course_material),
                    onClick = { viewModel.processIntent(CourseDetailsIntent.ClickUploadMaterial) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Top Navigation Bar with Course Options Menu
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

                    // Top Bar Popup Menu (Edit Course & Delete Course)
                    AppPopupMenu(
                        expanded = isCourseMenuExpanded,
                        onDismissRequest = { isCourseMenuExpanded = false },
                        items = listOf(
                            PopupMenuItem(
                                title = stringResource(R.string.edit_course),
                                icon = Icons.Outlined.Edit,
                                color = Color(0xFF374151),
                                height = 45.dp,
                                padding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                onClick = {
                                    viewModel.processIntent(CourseDetailsIntent.EditCourse)
                                }
                            ),
                            PopupMenuItem(
                                title = stringResource(R.string.delete_course),
                                icon = Icons.Outlined.Delete,
                                color = Color(0xFFEF4444),
                                height = 49.dp,
                                padding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
                                onClick = {
                                    viewModel.processIntent(CourseDetailsIntent.DeleteCourse)
                                }
                            )
                        )
                    )
                }
            }

            // 2. Screen Title
            Text(
                text = state.courseTitle,
                color = Theme.colorScheme.text.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.xs)
            )

            Spacer(modifier = Modifier.height(Theme.spacing.md))

            // 3. Custom Tabs
            AppPrimaryTabs(
                tabs = state.tabs,
                selectedTabIndex = state.selectedTabIndex,
                onTabSelected = { index ->
                    viewModel.processIntent(CourseDetailsIntent.SelectTab(index))
                }
            )

            // 4. Scrollable Content Area
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
            ) {
                item {
                    Spacer(modifier = Modifier.height(Theme.spacing.md))
                }

                if (state.selectedTabIndex == 0) {
                    items(state.materials, key = { it.id }) { document ->
                        Box {
                            AppDocumentCard(
                                title = document.title,
                                pageCount = document.pageCount,
                                fileSize = document.fileSize,
                                fileExtension = document.fileExtension,
                                onClick = {
                                    viewModel.processIntent(
                                        CourseDetailsIntent.ClickDocument(
                                            document.id
                                        )
                                    )
                                },
                                onMoreClick = {
                                    expandedDocumentId = document.id
                                }
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 8.dp, end = 8.dp) // Adjusts alignment directly over/near the 3 dots icon
                            ) {
                            // Document Card Popup Menu (Open for extension: easily add view/download/share items here)
                            AppPopupMenu(
                                expanded = expandedDocumentId == document.id,
                                onDismissRequest = { expandedDocumentId = null },
                                items = listOf(
                                    PopupMenuItem(
                                        title = stringResource(R.string.delete),
                                        icon = Icons.Outlined.Delete,
                                        color = Color(0xFFEF4444),
                                        height = 49.dp,
                                        padding = PaddingValues(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 16.dp,
                                            bottom = 12.dp
                                        ),
                                        onClick = {
                                            viewModel.processIntent(
                                                CourseDetailsIntent.DeleteDocument(
                                                    document.id
                                                )
                                            )
                                        }
                                    )
                                    // Easily extensible: Add more items here (e.g., Download, Share) in the future without layout changes.
                                )
                            )
                        }
                        }
                    }
                } else {
                    item {
                        CourseProgressCard(
                            completedTasks = state.completedTasks,
                            totalTasks = state.totalTasks,
                            percentage = state.progressPercentage
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(state.taskFilters.size) { index ->
                                val filter = state.taskFilters[index]
                                AppChip(
                                    label = filter,
                                    selected = state.selectedTaskFilterIndex == index,
                                    onSelectedChange = { 
                                        viewModel.processIntent(CourseDetailsIntent.SelectTaskFilter(index)) 
                                    }
                                )
                            }
                        }
                    }
                    
                    val todayTasks = state.tasks.filter { it.isToday }
                    if (todayTasks.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Today Tasks",
                                style = Theme.typography.title.medium.copy(fontWeight = FontWeight.Bold),
                                color = Theme.colorScheme.text.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(todayTasks, key = { it.id }) { task ->
                            AppTaskCard(
                                title = task.title,
                                duration = task.duration,
                                priority = task.priority,
                                isCompleted = task.isCompleted,
                                onClick = {
                                    viewModel.processIntent(CourseDetailsIntent.ClickTask(task.id))
                                }
                            )
                        }
                    }
                    
                    val upcomingTasks = state.tasks.filter { !it.isToday }
                    if (upcomingTasks.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Upcoming Tasks",
                                style = Theme.typography.title.medium.copy(fontWeight = FontWeight.Bold),
                                color = Theme.colorScheme.text.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(upcomingTasks, key = { it.id }) { task ->
                            AppTaskCard(
                                title = task.title,
                                duration = task.duration,
                                priority = task.priority,
                                isCompleted = task.isCompleted,
                                onClick = {
                                    viewModel.processIntent(CourseDetailsIntent.ClickTask(task.id))
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
    }
}