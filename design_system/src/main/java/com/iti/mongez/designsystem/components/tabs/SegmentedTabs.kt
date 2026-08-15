package com.iti.mongez.designsystem.components.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Data class representing a single tab item in the Segmented Control.
 */

/**
 * A reusable segmented control component for switching between parallel views.
 * Strictly uses Mongez AppColorScheme tokens to support dynamic light/dark theming.
 */
@Composable
fun AppSegmentedTabs(
    items: List<SegmentedTabItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Relying strictly on the provided AppColorScheme
    val trackBackgroundColor = Theme.colorScheme.surface.surfaceVariant
    val selectedTabBackgroundColor = Theme.colorScheme.surface.surface
    val activeContentColor = Theme.colorScheme.brand.primary
    val inactiveContentColor = Theme.colorScheme.text.secondary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = trackBackgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex

            val tabBackgroundColor = if (isSelected) selectedTabBackgroundColor else Color.Transparent
            val contentColor = if (isSelected) activeContentColor else inactiveContentColor

            // Only apply the drop shadow if the tab is currently selected
            val shadowElevation = if (isSelected) 1.dp else 0.dp
            val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .shadow(
                        elevation = shadowElevation,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Theme.colorScheme.border.primary.copy(alpha = 0.05f),
                        ambientColor = Theme.colorScheme.border.primary.copy(alpha = 0.05f)
                    )
                    .background(
                        color = tabBackgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (item.iconPainter != null) {
                        Icon(
                            painter = item.iconPainter,
                            contentDescription = "${item.title} icon",
                            tint = contentColor,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = item.title,
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = fontWeight,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Segmented Tabs - Light Theme")
@Composable
private fun AppSegmentedTabsLightPreview() {
    MongezTheme(darkTheme = false) {
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        val tabs = listOf(
            SegmentedTabItem(
                title = "Online Course",
                iconPainter = ColorPainter(Theme.colorScheme.brand.primary)
            ),
            SegmentedTabItem(
                title = "Upload Material",
                iconPainter = ColorPainter(Theme.colorScheme.text.secondary)
            )
        )

        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(24.dp)
        ) {
            AppSegmentedTabs(
                items = tabs,
                selectedIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
        }
    }
}

@Preview(showBackground = true, name = "Segmented Tabs - Dark Theme")
@Composable
private fun AppSegmentedTabsDarkPreview() {
    MongezTheme(darkTheme = true) {
        var selectedTabIndex by remember { mutableIntStateOf(1) } // Default to second tab to check state

        val tabs = listOf(
            SegmentedTabItem(
                title = "Online Course",
                iconPainter = ColorPainter(Theme.colorScheme.text.secondary)
            ),
            SegmentedTabItem(
                title = "Upload Material",
                iconPainter = ColorPainter(Theme.colorScheme.brand.primary)
            )
        )

        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(24.dp)
        ) {
            AppSegmentedTabs(
                items = tabs,
                selectedIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
        }
    }
}