package com.iti.mongez.designsystem.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Avatar component with initials or placeholder.
 *
 * @param modifier Modifier.
 * @param initials One or two letter initials to display.
 * @param size Diameter of the avatar.
 */
@Composable
fun AppAvatar(
    modifier: Modifier = Modifier,
    initials: String? = null,
    size: Dp = 40.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Theme.colorScheme.brand.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (initials != null) {
            Text(
                text = initials.take(2).uppercase(),
                style = Theme.typography.label.medium,
                color = Theme.colorScheme.brand.onPrimaryContainer,
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "Profile",
                tint = Theme.colorScheme.brand.onPrimaryContainer,
                modifier = Modifier.size(size * 0.6f),
            )
        }
    }
}

@Preview(showBackground = true, name = "Avatars")
@Composable
private fun AvatarPreview() {
    MongezTheme {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            AppAvatar(initials = "AB")
            AppAvatar(initials = "M")
            AppAvatar() // Placeholder
        }
    }
}
