package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.rounded.Cached
import androidx.compose.material.icons.rounded.CheckBox
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.card.AppCard
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onEventTypeSelected: (String) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Theme.radius.dialog),
            color = Theme.colorScheme.surface.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.md)
        ) {
            Box(modifier = Modifier.padding(Theme.spacing.xl)) {
                AddEventDialogContent(
                    onDismiss = onDismiss,
                    onEventTypeSelected = onEventTypeSelected
                )
            }
        }
    }
}

@Composable
fun AddEventDialogContent(
    onDismiss: () -> Unit,
    onEventTypeSelected: (String) -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.add_new_event_title),
            style = Theme.typography.title.medium,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Theme.spacing.xl),
            textAlign = TextAlign.Center
        )
        val eventTypes = listOf(
            EventTypeUiModel(
                id = "quiz",
                title = stringResource(R.string.event_quiz_title),
                description = stringResource(R.string.event_quiz_desc),
                icon = Icons.Rounded.CheckBox,
                containerColor = Theme.colorScheme.brand.primaryContainer,
                iconColor = Theme.colorScheme.brand.primary
            ),
            EventTypeUiModel(
                id = "assignment",
                title = stringResource(R.string.event_assignment_title),
                description = stringResource(R.string.event_assignment_desc),
                icon = Icons.AutoMirrored.Rounded.Assignment,
                containerColor = Theme.colorScheme.state.warningContainer,
                iconColor = Theme.colorScheme.state.warning
            ),
            EventTypeUiModel(
                id = "midterm",
                title = stringResource(R.string.event_midterm_title),
                description = stringResource(R.string.event_midterm_desc),
                icon = Icons.Rounded.Cached,
                containerColor = Theme.colorScheme.state.errorContainer,
                iconColor = Theme.colorScheme.state.error
            ),
            EventTypeUiModel(
                id = "project",
                title = stringResource(R.string.event_project_title),
                description = stringResource(R.string.event_project_desc),
                icon = Icons.Rounded.GridView,
                containerColor = Theme.colorScheme.state.infoContainer,
                iconColor = Theme.colorScheme.state.info
            )
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            eventTypes.forEach { eventType ->
                EventTypeItem(
                    eventType = eventType,
                    onClick = {
                        onEventTypeSelected(eventType.id)
                        onDismiss()
                    }
                )
            }

            Spacer(modifier = Modifier.height(Theme.spacing.lg))

            AppButton(
                text = stringResource(R.string.action_cancel),
                onClick = onDismiss,
                variant = AppButtonVariant.Secondary,
                fullWidth = true
            )
        }
    }
}

@Composable
private fun EventTypeItem(
    eventType: EventTypeUiModel,
    onClick: () -> Unit
) {
    AppCard(
        onClick = onClick,
        elevation = 0.dp,
        containerColor = Theme.colorScheme.surface.surfaceLow
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(Theme.spacing.huge)
                    .background(eventType.containerColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = eventType.icon,
                    contentDescription = null,
                    tint = eventType.iconColor,
                    modifier = Modifier.size(Theme.spacing.xl)
                )
            }

            Spacer(modifier = Modifier.width(Theme.spacing.lg))

            Column {
                Text(
                    text = eventType.title,
                    style = Theme.typography.title.medium,
                    color = Theme.colorScheme.text.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = eventType.description,
                    style = Theme.typography.body.small,
                    color = Theme.colorScheme.text.secondary
                )
            }
        }
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

@Preview(showBackground = true)
@Composable
private fun AddEventDialogPreview() {
    MongezTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AddEventDialogContent(
                onDismiss = {},
                onEventTypeSelected = {}
            )
        }
    }
}
