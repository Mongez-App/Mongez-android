package com.iti.mongez.designsystem.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.R
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

import androidx.compose.ui.graphics.Color

@Composable
fun AppTaskCard(
    title: String,
    duration: String,
    priorityTextRes: Int,
    priorityColor: Color,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Theme.colorScheme.text.primary.copy(alpha = 0.05f),
                spotColor = Theme.colorScheme.text.primary.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colorScheme.surface.background)
            .border(
                width = 1.dp,
                color = Theme.colorScheme.text.disabled,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(enabled = !isCompleted) { onClick() }
            .padding(16.dp) // 16px padding inside the card
            .then(if (isCompleted) Modifier.alpha(0.5f) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp) // 16px gap between check and text
    ) {
        // Left Element: Checkbox Toggle
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) Theme.colorScheme.state.success else Theme.colorScheme.surface.background
                )
                .border(
                    width = if (isCompleted) 0.dp else 2.dp, // Thicker border for unselected state
                    color = if (isCompleted) Theme.colorScheme.state.success else Theme.colorScheme.text.disabled,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Theme.colorScheme.surface.background, // White checkmark
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Right Element: Text Content Stack
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Task Title
            Text(
                text = title,
                color = Theme.colorScheme.text.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            // Task Metadata Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Duration
                Text(
                    text = duration,
                    color = Theme.colorScheme.text.tertiary, // Maps to #9CA3AF gracefully
                    fontSize = 12.sp, // Bumped slightly from 10sp for better standard readability, but keeps ratio
                    fontWeight = FontWeight.Normal
                )

                // Divider Dot
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.border.secondary)
                )

                // Priority Tag
                Text(
                    text = stringResource(id = priorityTextRes),
                    color = priorityColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.25).sp
                )
            }
        }
    }
}

/**
 * Preview exactly matching the image_8c665c.png screenshot sequence.
 */
@Preview(showBackground = true, name = "Task Cards List")
@Composable
private fun AppTaskCardsListPreview() {
    MongezTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Space between stacked cards
        ) {

            AppTaskCard(
                title = "Read Chapter 4",
                duration = "45 min",
                priorityTextRes = R.string.priority_high,
                priorityColor = Theme.colorScheme.state.error,
                isCompleted = true,
                onClick = {}
            )

            AppTaskCard(
                title = "Practice DFS Problems",
                duration = "30 min",
                priorityTextRes = R.string.priority_medium,
                priorityColor = Theme.colorScheme.state.warning,
                isCompleted = false,
                onClick = {}
            )

            AppTaskCard(
                title = "Finish Quiz",
                duration = "20 min",
                priorityTextRes = R.string.priority_low,
                priorityColor = Theme.colorScheme.state.success,
                isCompleted = false,
                onClick = {}
            )
        }
    }
}