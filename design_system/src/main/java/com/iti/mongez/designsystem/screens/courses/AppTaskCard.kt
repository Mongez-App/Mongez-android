package com.iti.mongez.designsystem.screens.courses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun AppTaskCard(
    title: String,
    duration: String,
    priority: String,
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val priorityColor = when (priority.uppercase()) {
        "HIGH" -> Theme.colorScheme.state.error
        "MEDIUM" -> Theme.colorScheme.state.warning
        "LOW" -> Theme.colorScheme.state.success
        else -> Theme.colorScheme.text.secondary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(Theme.radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colorScheme.surface.background
        ),
        border = BorderStroke(1.dp, Theme.colorScheme.surface.surfaceHigh),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Theme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox Circle
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) Theme.colorScheme.state.success else Color.Transparent
                    )
                    .then(
                        if (!isCompleted) Modifier.border(2.dp, Theme.colorScheme.surface.surfaceHigh, CircleShape)
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Theme.colorScheme.brand.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(Theme.spacing.lg))

            // Task Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = Theme.typography.label.large.copy(fontWeight = FontWeight.SemiBold),
                    color = Theme.colorScheme.text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(Theme.spacing.xs))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = duration,
                        style = Theme.typography.body.small,
                        color = Theme.colorScheme.text.tertiary
                    )
                    Text(
                        text = " • ",
                        style = Theme.typography.body.small,
                        color = Theme.colorScheme.text.tertiary
                    )
                    Text(
                        text = priority.uppercase(),
                        style = Theme.typography.body.small.copy(fontWeight = FontWeight.Bold),
                        color = priorityColor,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppTaskCardPreview() {
    MongezTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTaskCard(
                title = "Read Chapter 4",
                duration = "45 min",
                priority = "HIGH",
                isCompleted = true
            )
            AppTaskCard(
                title = "Practice DFS Problems",
                duration = "30 min",
                priority = "MEDIUM",
                isCompleted = false
            )
            AppTaskCard(
                title = "Finish Quiz",
                duration = "20 min",
                priority = "LOW",
                isCompleted = false
            )
        }
    }
}
