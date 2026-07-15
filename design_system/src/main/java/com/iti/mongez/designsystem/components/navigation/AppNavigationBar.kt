package com.iti.mongez.designsystem.components.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Data class representing a single navigation item.
 */
@Immutable
data class AppNavigationBarItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val contentDescription: String = label,
)

/**
 * Bottom navigation bar for the Mongez application.
 *
 * - 80dp height
 * - Purple active state, Gray inactive
 * - Labels always visible
 * - No shifting animation
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
    NavigationBar(
        modifier = modifier.height(80.dp),
        containerColor = Theme.colorScheme.navigation.background,
        tonalElevation = Theme.elevation.none,
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.contentDescription,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = Theme.typography.label.small,
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Theme.colorScheme.navigation.activeIcon,
                    selectedTextColor = Theme.colorScheme.navigation.activeLabel,
                    unselectedIconColor = Theme.colorScheme.navigation.inactiveIcon,
                    unselectedTextColor = Theme.colorScheme.navigation.inactiveLabel,
                    indicatorColor = Theme.colorScheme.navigation.indicator,
                ),
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

private val previewItems = listOf(
    AppNavigationBarItem(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    AppNavigationBarItem(
        label = "Courses",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
    ),
    AppNavigationBarItem(
        label = "Roadmap",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
    ),
    AppNavigationBarItem(
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
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
