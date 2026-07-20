package com.iti.mongez.designsystem.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Data model representing an item in the alert's task list.
 * @param dotColor Optional custom color for this specific task's dot. If null, falls back to the alert's global dot color.
 */
data class AlertTaskItem(
    val id: String,
    val name: String,
    val time: String,
    val dotColor: Color? = null
)

/**
 * Highly reusable Task Alert Dialog Component.
 */
@Composable
fun AppTaskAlert(
    title: String,
    message: String,
    primaryButtonText: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    tasks: List<AlertTaskItem> = emptyList(),
    secondaryButtonText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    onDismissRequest: () -> Unit = {},

    // Icon Customization (Pass an ImageVector directly OR use iconContent for custom composables)
    icon: ImageVector? = null,
    iconTint: Color = Theme.colorScheme.state.warning,
    iconBackgroundColor: Color = Theme.colorScheme.state.warningContainer,
    iconContent: (@Composable () -> Unit)? = null,

    // Styling & Colors
    taskDotColor: Color = Theme.colorScheme.state.warning,
    surfaceBackgroundColor: Color = Theme.colorScheme.surface.surface,
    taskContainerBackground: Color = Theme.colorScheme.surface.surfaceVariant,
    taskDividerColor: Color = Theme.colorScheme.border.secondary,
    titleColor: Color = Theme.colorScheme.text.primary,
    messageColor: Color = Theme.colorScheme.text.secondary,
    taskNameColor: Color = Theme.colorScheme.text.primary,
    taskTimeColor: Color = Theme.colorScheme.text.secondary
) {
    Surface(
        modifier = modifier
            .width(340.dp)
            .shadow(
                elevation = Theme.elevation.lg,
                shape = RoundedCornerShape(26.dp),
                ambientColor = Theme.colorScheme.text.primary.copy(alpha = 0.15f),
                spotColor = Theme.colorScheme.text.primary.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(26.dp),
        color = surfaceBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Theme.spacing.xl,
                    vertical = Theme.spacing.xxl
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Alert Icon Container (Supports ImageVector or Custom Composable)
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                when {
                    iconContent != null -> iconContent()
                    icon != null -> Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = iconTint
                    )
                }
            }

            Spacer(modifier = Modifier.height(Theme.spacing.lg))

            // 2. Alert Title
            Text(
                text = title,
                style = Theme.typography.title.large,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Theme.spacing.sm))

            // 3. Alert Description Text
            Text(
                text = message,
                style = Theme.typography.body.medium,
                color = messageColor,
                textAlign = TextAlign.Center
            )

            // 4. Task List Container
            if (tasks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Theme.spacing.lg))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Theme.radius.lg))
                        .background(taskContainerBackground)
                        .padding(
                            horizontal = Theme.spacing.md,
                            vertical = Theme.spacing.xs
                        )
                ) {
                    tasks.forEachIndexed { index, task ->
                        if (index > 0) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = taskDividerColor
                            )
                        }
                        TaskRowItem(
                            task = task,
                            globalDotColor = taskDotColor,
                            nameColor = taskNameColor,
                            timeColor = taskTimeColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Theme.spacing.xl))

            // 5. Actions / Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.sm)
            ) {
                AppButton(
                    text = primaryButtonText,
                    onClick = onPrimaryClick,
                    variant = AppButtonVariant.Primary,
                    fullWidth = true
                )

                if (secondaryButtonText != null && onSecondaryClick != null) {
                    AppButton(
                        text = secondaryButtonText,
                        onClick = onSecondaryClick,
                        variant = AppButtonVariant.Secondary,
                        fullWidth = true,
                        containerColor = taskContainerBackground
                    )
                }
            }
        }
    }
}

/**
 * Individual row element for the task list.
 */
@Composable
private fun TaskRowItem(
    task: AlertTaskItem,
    globalDotColor: Color,
    nameColor: Color,
    timeColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Task Status Dot (Respects individual task dotColor, falls back to globalDotColor)
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(task.dotColor ?: globalDotColor)
        )

        Spacer(modifier = Modifier.width(Theme.spacing.sm))

        // Task Name
        Text(
            text = task.name,
            style = Theme.typography.body.medium.copy(fontWeight = FontWeight.Bold),
            color = nameColor,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        // Task Time
        Text(
            text = task.time,
            style = Theme.typography.label.medium,
            color = timeColor
        )
    }
}
@Preview(name = "Task Alert - Default", showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
private fun AppTaskAlertDefaultPreview() {
    MongezTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AppTaskAlert(
                title = "You still have tasks left",
                message = "You didn't get to finish 2 tasks today. What would you like to do with them?",
                icon = androidx.compose.material.icons.Icons.Default.Warning,
                tasks = listOf(
                    AlertTaskItem(id = "1", name = "Practice DFS Problems", time = "30 min"),
                    AlertTaskItem(id = "2", name = "Finish Quiz", time = "20 min")
                ),
                primaryButtonText = "Reschedule to Tomorrow",
                onPrimaryClick = {},
                secondaryButtonText = "Mark All as Done",
                onSecondaryClick = {}
            )
        }
    }
}

@Preview(name = "Task Alert - Custom Icon & Task Dots", showBackground = true, backgroundColor = 0xFF1F2937)
@Composable
private fun AppTaskAlertCustomPreview() {
    MongezTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AppTaskAlert(
                title = "Pending Action Required",
                message = "Please review your remaining items before midnight.",
                icon = androidx.compose.material.icons.Icons.Default.Info,
                iconTint = Color(0xFF3B82F6),
                iconBackgroundColor = Color(0xFF3B82F6).copy(alpha = 0.15f),
                tasks = listOf(
                    AlertTaskItem(id = "1", name = "Review PR #42", time = "15 min", dotColor = Color(0xFF10B981)),
                    AlertTaskItem(id = "2", name = "Update Dependencies", time = "10 min", dotColor = Color(0xFFEC4899))
                ),
                primaryButtonText = "Continue",
                onPrimaryClick = {},
                secondaryButtonText = "Close",
                onSecondaryClick = {}
            )
        }
    }
}