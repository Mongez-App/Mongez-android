package com.iti.mongez.designsystem.components.fab

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Floating Action Button.
 *
 * @param onClick Callback on tap.
 * @param modifier Modifier.
 * @param icon Icon to display. Defaults to Add (+).
 * @param contentDescription Accessibility description.
 */
@Composable
fun AppFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Add,
    contentDescription: String = "Add",
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        shape = CircleShape,
        containerColor = Theme.colorScheme.brand.primary,
        contentColor = Theme.colorScheme.brand.onPrimary,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview(name = "FAB")
@Composable
private fun FabPreview() {
    MongezTheme {
        AppFab(onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
