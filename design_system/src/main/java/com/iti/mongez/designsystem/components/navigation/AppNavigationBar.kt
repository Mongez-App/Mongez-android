package com.iti.mongez.designsystem.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.R
import com.iti.mongez.designsystem.foundation.modifier.mongezShadow
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Data class representing a single navigation item.
 */
@Immutable
data class AppNavigationBarItem(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val contentDescription: String = label,
)

/**
 * Floating bottom navigation bar matching custom pill design.
 *
 * - Floating with horizontal and bottom padding
 * - Pill-shaped (fully rounded corners)
 * - Dot indicator for active state (no labels)
 *
 * @param items List of navigation items.
 * @param selectedIndex Currently selected index.
 * @param onItemSelected Callback with the index of the tapped item.
 * @param modifier Modifier for external layout.
 */
@Composable
fun AppNavigationBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.xl, vertical = Theme.radius.xxl)
            .mongezShadow(
                color = Theme.colorScheme.brand.primary.copy(alpha = 0.58f),
                borderRadius = Theme.radius.xxl,
                blurRadius = 10.dp
            ),
        shape = RoundedCornerShape(Theme.radius.xxl),
        color = Theme.colorScheme.navigation.background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = Theme.spacing.lg),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex

                // Interaction source to remove the default ripple effect for a cleaner tap
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null, // Removes ripple. Add custom ripple here if desired.
                            onClick = { onItemSelected(index) }
                        )
                        .padding(horizontal = Theme.spacing.md, vertical = Theme.spacing.sm),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = if (isSelected) item.selectedIcon else item.unselectedIcon),
                        contentDescription = item.contentDescription,
                        tint = if (isSelected) Theme.colorScheme.navigation.activeIcon else Theme.colorScheme.navigation.inactiveIcon,
                        modifier = Modifier.size(Theme.spacing.xl)
                    )

                    Spacer(modifier = Modifier.height(Theme.spacing.xs))

                    // The custom dot indicator below the icon
                    Box(
                        modifier = Modifier
                            .size(Theme.spacing.xs)
                            .clip(CircleShape)
                            .background(
                                color = if (isSelected) Theme.colorScheme.navigation.activeIcon else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

private val previewItems = listOf(
    AppNavigationBarItem(
        label = "Home",
        selectedIcon = R.drawable.ic_sparkle,
        unselectedIcon = R.drawable.ic_sparkle,
    ),
    AppNavigationBarItem(
        label = "Courses",
        selectedIcon = R.drawable.ic_sparkle,
        unselectedIcon = R.drawable.ic_sparkle,
    ),
    AppNavigationBarItem(
        label = "Roadmap",
        selectedIcon = R.drawable.ic_sparkle,
        unselectedIcon = R.drawable.ic_sparkle,
    ),
    AppNavigationBarItem(
        label = "Profile",
        selectedIcon = R.drawable.ic_sparkle,
        unselectedIcon = R.drawable.ic_sparkle,
    ),
)

@Preview(showBackground = true, name = "Navigation Bar")
@Composable
private fun NavigationBarPreview() {
    MongezTheme {
        AppNavigationBar(
            items = previewItems,
            selectedIndex = 0,
            onItemSelected = {},
        )
    }
}

@Preview(showBackground = true, name = "Navigation Bar - Dark")
@Composable
private fun NavigationBarDarkPreview() {
    MongezTheme(darkTheme = true) {
        AppNavigationBar(
            items = previewItems,
            selectedIndex = 1,
            onItemSelected = {},
        )
    }
}
