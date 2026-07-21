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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.R
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun AppStreakBadge(
    streakCount: Int,
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Reduced spacing for a tighter badge
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
                    style = Theme.typography.title.medium, // Downscaled from headline.medium
                    color = if (isActive) {
                        Theme.colorScheme.state.warning
                    } else {
                        Theme.colorScheme.text.secondary
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = stringResource(id = R.string.streak_day),
                    style = Theme.typography.title.medium, // Downscaled from headline.medium
                    color = Theme.colorScheme.text.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.alignByBaseline()
                )
            }

            // Bottom Line: "Streak"
            Text(
                text = stringResource(id = R.string.streak_title),
                style = Theme.typography.label.large, // Downscaled from headline.medium
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold
            )
        }

        // Right Side: Large Icon
        Text(
            text = if (isActive) "🔥" else "🧊",
            fontSize = 32.sp // Scaled down significantly from 56.sp to fit the UI
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