package com.iti.mongez.designsystem.components.snackbar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

enum class AppSnackbarType {
    Success,
    Error,
    Warning,
    Info,
}

/**
 * Styled snackbar content for lightweight feedback.
 *
 * @param message Message text.
 * @param type Visual variant.
 * @param modifier Modifier.
 */
@Composable
fun AppSnackbarContent(
    message: String,
    type: AppSnackbarType,
    modifier: Modifier = Modifier,
) {
    val (icon, iconColor, containerColor, borderColor) = when (type) {
        AppSnackbarType.Success -> SnackbarStyle(
            Icons.Rounded.CheckCircle,
            Theme.colorScheme.state.success,
            Theme.colorScheme.state.successContainer,
            Theme.colorScheme.state.success,
        )
        AppSnackbarType.Error -> SnackbarStyle(
            Icons.Rounded.Error,
            Theme.colorScheme.state.error,
            Theme.colorScheme.state.errorContainer,
            Theme.colorScheme.state.error,
        )
        AppSnackbarType.Warning -> SnackbarStyle(
            Icons.Rounded.Warning,
            Theme.colorScheme.state.warning,
            Theme.colorScheme.state.warningContainer,
            Theme.colorScheme.state.warning,
        )
        AppSnackbarType.Info -> SnackbarStyle(
            Icons.Rounded.Info,
            Theme.colorScheme.state.info,
            Theme.colorScheme.state.infoContainer,
            Theme.colorScheme.state.info,
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Theme.radius.md),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.padding(Theme.spacing.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(Theme.spacing.md))
            Text(
                text = message,
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.text.primary,
            )
        }
    }
}

private data class SnackbarStyle(
    val icon: ImageVector,
    val iconColor: Color,
    val containerColor: Color,
    val borderColor: Color,
)

@Preview(showBackground = true, name = "Snackbars")
@Composable
private fun SnackbarPreview() {
    MongezTheme {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            AppSnackbarContent("Course added successfully!", AppSnackbarType.Success)
            AppSnackbarContent("Failed to save changes.", AppSnackbarType.Error)
            AppSnackbarContent("Deadline approaching!", AppSnackbarType.Warning)
            AppSnackbarContent("New update available.", AppSnackbarType.Info)
        }
    }
}
