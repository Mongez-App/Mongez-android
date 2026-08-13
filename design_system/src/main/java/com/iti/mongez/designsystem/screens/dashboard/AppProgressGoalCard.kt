package com.iti.mongez.designsystem.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun AppProgressGoalCard(
    title: String,
    currentValue: Int,
    totalValue: Int,
    unit: String,
    tintColor: Color,
    modifier: Modifier = Modifier
) {
    val progressFraction = if (totalValue > 0) {
        (currentValue.toFloat() / totalValue.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Column(
        modifier = modifier
            .size(width = 140.dp, height = 86.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .background(Theme.colorScheme.surface.background, shape = RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = tintColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            color = tintColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentValue.toString(),
                    color = tintColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = " / $totalValue",
                    color = Theme.colorScheme.text.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.alignByBaseline()
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = unit,
                    color = Theme.colorScheme.text.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.alignByBaseline()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    // Use surfaceVariant for a clearer gray track when there are no tasks
                    .background(Theme.colorScheme.surface.surfaceVariant)
            ) {
                if (progressFraction > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progressFraction)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(tintColor)
                    )
                }
            }
        }
    }
}

/**
 * Preview matching the provided screenshot exactly, utilizing your theme's semantic colors.
 */
@Preview(showBackground = true, backgroundColor = 0xFFF9F9FF, name = "Goal Cards Row")
@Composable
private fun AppProgressGoalCardsPreview() {
    MongezTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Blue Card -> Using state.info
            AppProgressGoalCard(
                title = "Today's Goal",
                currentValue = 2,
                totalValue = 5,
                unit = "tasks",
                tintColor = Theme.colorScheme.state.info
            )

            // Green Card -> Using state.success
            AppProgressGoalCard(
                title = "Weekly Progress",
                currentValue = 12,
                totalValue = 20,
                unit = "hours",
                tintColor = Theme.colorScheme.state.success
            )

            // Purple Card -> Using brand.primary
            AppProgressGoalCard(
                title = "Monthly",
                currentValue = 45,
                totalValue = 80,
                unit = "tasks",
                tintColor = Theme.colorScheme.brand.primary
            )
        }
    }
}