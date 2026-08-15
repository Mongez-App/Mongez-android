package com.iti.mongez.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.foundation.layout.Column

@Composable
fun SettingItem(
    icon: ImageVector,
    iconContainerColor: Color,
    iconTint: Color,
    title: String,
    titleColor: Color = Theme.colorScheme.text.primary,
    action: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Theme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Theme.spacing.huge)
                .background(iconContainerColor, RoundedCornerShape(Theme.radius.md)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(Theme.spacing.lg)
            )
        }

        Spacer(modifier = Modifier.width(Theme.spacing.lg))

        Text(
            text = title,
            style = Theme.typography.label.medium,
            color = titleColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        if (action != null) {
            action()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingItemPreview() {
    MongezTheme {
        Column(modifier = Modifier.padding(Theme.spacing.md)) {
            SettingItem(
                icon = Icons.Rounded.Notifications,
                iconContainerColor = Theme.colorScheme.brand.primaryContainer,
                iconTint = Theme.colorScheme.brand.primary,
                title = "Notifications",
                onClick = {}
            )
            SettingItem(
                icon = Icons.Rounded.Notifications,
                iconContainerColor = Theme.colorScheme.state.errorContainer,
                iconTint = Theme.colorScheme.state.error,
                title = "Logout",
                titleColor = Theme.colorScheme.state.error,
                action = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Theme.colorScheme.text.secondary
                    )
                },
                onClick = {}
            )
        }
    }
}