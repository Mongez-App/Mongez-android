package com.iti.mongez.designsystem.components.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

enum class AppBadgeType {
    Primary,
    Success,
    Warning,
    Error,
    Info,
}

/**
 * Small badge for notifications, priority, or status.
 *
 * @param text Badge text (e.g. "3", "NEW", "HOT").
 * @param modifier Modifier.
 * @param type Visual style variant.
 */
@Composable
fun AppBadge(
    text: String,
    modifier: Modifier = Modifier,
    type: AppBadgeType = AppBadgeType.Primary,
) {
    val (bgColor, contentColor) = when (type) {
        AppBadgeType.Primary -> Theme.colorScheme.brand.primary to Theme.colorScheme.brand.onPrimary
        AppBadgeType.Success -> Theme.colorScheme.state.success to Color.White
        AppBadgeType.Warning -> Theme.colorScheme.state.warning to Color.White
        AppBadgeType.Error -> Theme.colorScheme.state.error to Color.White
        AppBadgeType.Info -> Theme.colorScheme.state.info to Color.White
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Theme.radius.full))
            .background(bgColor)
            .padding(horizontal = Theme.spacing.sm, vertical = Theme.spacing.xxs),
    ) {
        Text(
            text = text,
            style = Theme.typography.label.extraSmall,
            color = contentColor,
        )
    }
}

@Preview(showBackground = true, name = "Badges")
@Composable
private fun BadgePreview() {
    MongezTheme {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            AppBadge(text = "3")
            AppBadge(text = "NEW", type = AppBadgeType.Success)
            AppBadge(text = "HIGH", type = AppBadgeType.Error)
            AppBadge(text = "MEDIUM", type = AppBadgeType.Warning)
        }
    }
}
