package com.iti.mongez.presentation.studyroom.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.R
import com.iti.mongez.designsystem.components.button.AppGlowIconButton
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.studyroom.components.ChatBubble
import com.iti.mongez.presentation.studyroom.components.EndSessionDialog
import com.iti.mongez.presentation.studyroom.contract.StudyRoomEffect
import com.iti.mongez.presentation.studyroom.contract.StudyRoomIntent
import com.iti.mongez.presentation.studyroom.uiState.StudyRoomState
import com.iti.mongez.presentation.studyroom.viewmodel.StudyRoomViewModel
import kotlinx.coroutines.flow.collectLatest

data class ChatMessage(val text: String, val isUser: Boolean)

@Composable
fun StudyRoomScreen(
    taskId: String,
    title: String,
    courseId: String,
    durationMinutes: Int,
    onNavigateBack: () -> Unit,
    viewModel: StudyRoomViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(taskId, title, courseId, durationMinutes) {
        viewModel.handleIntent(StudyRoomIntent.Initialize(taskId, title, courseId, durationMinutes))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is StudyRoomEffect.NavigateBack -> onNavigateBack()
                is StudyRoomEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    BackHandler {
        viewModel.handleIntent(StudyRoomIntent.ShowEndSessionDialog(true))
    }

    StudyRoomContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onIntent = viewModel::handleIntent
    )
}

@Composable
fun StudyRoomContent(
    state: StudyRoomState,
    snackbarHostState: SnackbarHostState,
    onIntent: (StudyRoomIntent) -> Unit
) {
    val minutes = (state.timeRemaining / 60).toString().padStart(2, '0')
    val seconds = (state.timeRemaining % 60).toString().padStart(2, '0')
    val timeString = "$minutes:$seconds"

    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = Theme.colorScheme.surface.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                AppSnackbarContent(
                    message = snackbarData.visuals.message,
                    type = com.iti.mongez.designsystem.components.snackbar.AppSnackbarType.Success
                )
            }
        },
        bottomBar = {
            // Chat Input Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
            ) {
                OutlinedTextField(
                    value = state.inputText,
                    onValueChange = { onIntent(StudyRoomIntent.UpdateInputText(it)) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Ask your AI tutor", color = Theme.colorScheme.text.tertiary) },
                    shape = RoundedCornerShape(Theme.radius.md),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Theme.colorScheme.border.primary,
                        focusedBorderColor = Theme.colorScheme.brand.primary,
                    )
                )
                IconButton(
                    onClick = { onIntent(StudyRoomIntent.SendMessage) },
                    modifier = Modifier
                        .size(Theme.spacing.huge)
                        .clip(CircleShape)
                        .border(1.dp, Theme.colorScheme.border.primary, CircleShape)
                        .background(Theme.colorScheme.surface.background)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = "Send",
                        tint = Theme.colorScheme.brand.primary,
                        modifier = Modifier.size(Theme.spacing.xl)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.title.ifEmpty { "Loading..." },
                    style = Theme.typography.headline.medium.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.text.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = Theme.spacing.md)
                )

                // Header Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pause/Play Button
                    AppGlowIconButton(
                        painter = if (state.isPaused) painterResource(id = R.drawable.play) else painterResource(id = R.drawable.pause),
                        contentDescription = if (state.isPaused) "Play" else "Pause",
                        onClick = { onIntent(StudyRoomIntent.ToggleTimer) },
                        iconTint = Theme.colorScheme.brand.primary,
                        glowColor = Theme.colorScheme.brand.primary
                    )

                    // End Session Button
                    AppGlowIconButton(
                        imageVector = Icons.Default.Check,
                        contentDescription = "End Session",
                        onClick = { onIntent(StudyRoomIntent.ShowEndSessionDialog(true)) },
                        iconTint = Theme.colorScheme.state.success,
                        glowColor = Theme.colorScheme.brand.primary
                    )
                }
            }

            // Timer row
            Row(
                modifier = Modifier.padding(horizontal = Theme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Theme.colorScheme.surface.background,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Theme.colorScheme.border.primary),
                    modifier = Modifier.padding(end = Theme.spacing.sm)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = Theme.spacing.md, vertical = Theme.spacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Timer",
                            tint = Theme.colorScheme.text.primary,
                            modifier = Modifier.size(Theme.spacing.lg)
                        )
                        Spacer(modifier = Modifier.width(Theme.spacing.xs))
                        Text(
                            text = timeString,
                            style = Theme.typography.label.large.copy(fontWeight = FontWeight.Bold),
                            color = Theme.colorScheme.text.primary
                        )
                    }
                }
                Text(
                    text = "/",
                    color = Theme.colorScheme.text.secondary,
                    modifier = Modifier.padding(horizontal = Theme.spacing.xs)
                )
                Surface(
                    shape = CircleShape,
                    color = Theme.colorScheme.surface.background,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Theme.colorScheme.border.primary),
                    modifier = Modifier.padding(start = Theme.spacing.sm)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = Theme.spacing.md, vertical = Theme.spacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Total Time",
                            tint = Theme.colorScheme.text.primary,
                            modifier = Modifier.size(Theme.spacing.lg)
                        )
                        Spacer(modifier = Modifier.width(Theme.spacing.xs))
                        Text(
                            text = String.format("%02d:%02d", state.totalDurationSeconds / 60, state.totalDurationSeconds % 60),
                            style = Theme.typography.label.large.copy(fontWeight = FontWeight.Bold),
                            color = Theme.colorScheme.text.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Theme.spacing.xl))

            HorizontalDivider(color = Theme.colorScheme.border.secondary, thickness = 1.dp)

            // Chat Interface
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
            ) {
                items(state.messages.size) { index ->
                    val msg = state.messages[index]
                    ChatBubble(
                        text = msg.text,
                        isUser = msg.isUser
                    )
                }
            }
        }
    }

    // End Session Alert Dialog
    if (state.showEndSessionDialog) {
        EndSessionDialog(
            onDismiss = { onIntent(StudyRoomIntent.ShowEndSessionDialog(false)) },
            onEndSession = { onIntent(StudyRoomIntent.EndSession(state.timeRemaining == 0)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StudyRoomScreenPreview() {
    MongezTheme {
        StudyRoomContent(
            state = StudyRoomState(title = "Dynamic Programming"),
            snackbarHostState = remember { SnackbarHostState() },
            onIntent = {}
        )
    }
}
