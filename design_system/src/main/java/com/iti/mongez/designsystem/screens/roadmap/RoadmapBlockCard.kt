package com.iti.mongez.designsystem.screens.roadmap

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun RoadmapBlockCard(
    courseName: String,
    topic: String,
    color: Color,
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
    eventTitle: String? = null,
    eventIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(Theme.radius.xl),
            colors = CardDefaults.cardColors(containerColor = color)
        ) {
            Box(modifier = Modifier.padding(Theme.spacing.lg)) {
                Text(
                    text = courseName,
                    style = Theme.typography.label.medium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.width(Theme.spacing.md))

        if (isCompleted) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Theme.colorScheme.state.success.copy(alpha = 0.1f), CircleShape)
                    .padding(4.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Theme.colorScheme.state.success,
                    modifier = Modifier.size(20.dp)
                )
            }
        } else if (eventTitle != null) {
            Card(
                shape = RoundedCornerShape(Theme.radius.xl),
                colors = CardDefaults.cardColors(containerColor = Theme.colorScheme.surface.surfaceVariant),
                modifier = Modifier.height(32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = Theme.spacing.md),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    eventIcon?.invoke()
                    Text(
                        text = eventTitle,
                        style = Theme.typography.label.small,
                        color = Theme.colorScheme.text.primary
                    )
                }
            }
        }
    }
}
