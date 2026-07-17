package com.iti.mongez.designsystem.screens.dashboard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A reusable streak badge component to display the user's current streak.
 * Matches the expanded two-line typography and large icon design.
 *
 * @param streakCount The number of consecutive days/actions completed.
 * @param modifier Modifier to be applied to the layout.
 * @param isActive Determines the visual state (active vs. inactive) of the streak.
 */
@Composable
fun AppStreakBadge(
    streakCount: Int,
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Space between text block and fire icon
    ) {

        // Left Side: Text Block
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            // Top Line: "12 Day"
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = streakCount.toString(),
                    // Assuming headline.medium or similar for the large text
                    style = Theme.typography.headline.medium,
                    color = if (isActive) {
                        Theme.colorScheme.state.warning
                    } else {
                        Theme.colorScheme.text.secondary
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Day",
                    style = Theme.typography.headline.medium,
                    color = Theme.colorScheme.text.primary, // Dark Navy/Black
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()
                )
            }

            // Bottom Line: "Streak"
            Text(
                text = "Streak",
                style = Theme.typography.headline.medium,
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold
            )
        }

        // Right Side: Large Icon
        // Rendered as text to keep the emoji, scaled up significantly to match the image
        Text(
            text = if (isActive) "🔥" else "🧊",
            fontSize = 56.sp
        )
    }
}

@Preview(showBackground = true, name = "Streak Badge - Active")
@Composable
private fun AppStreakBadgeActivePreview() {
    MongezTheme {
        AppStreakBadge(
            streakCount = 12,
            isActive = true,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true, name = "Streak Badge - Inactive")
@Composable
private fun AppStreakBadgeInactivePreview() {
    MongezTheme {
        AppStreakBadge(
            streakCount = 0,
            isActive = false,
            modifier = Modifier.padding(24.dp)
        )
    }
}