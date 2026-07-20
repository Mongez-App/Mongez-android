package com.iti.mongez.designsystem.components.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A reusable tab component adhering to the Mongez design system.
 * Updated with a larger selected font, slight upward vertical offset, and a thicker active indicator.
 */
@Composable
fun AppPrimaryTabs(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = Theme.colorScheme.surface.surface,
        // The 1px solid #F3F4F6 bottom border
        divider = {
            HorizontalDivider(
                thickness = 1.dp,
                color = Theme.colorScheme.border.secondary
            )
        },
        // The thicker active indicator (3.dp)
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    height = 3.dp,
                    color = Theme.colorScheme.brand.primary
                )
            }
        }
    ) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedTabIndex == index

            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                modifier = Modifier.height(38.dp),
                text = {
                    Text(
                        text = title,
                        fontSize = if (isSelected) 16.sp else 15.sp, // Bigger font for selected tab
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) {
                            Theme.colorScheme.brand.primary
                        } else {
                            Theme.colorScheme.text.secondary
                        },
                        modifier = if (isSelected) {
                            Modifier.offset(y = (-2).dp) // Places the selected tab slightly higher
                        } else {
                            Modifier
                        }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Primary Tabs - Light")
@Composable
private fun AppPrimaryTabsLightPreview() {
    var selectedTab by remember { mutableIntStateOf(0) }

    MongezTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.surface)
                .padding(Theme.spacing.lg)
        ) {
            AppPrimaryTabs(
                tabs = listOf("Materials", "Tasks"),
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}

@Preview(showBackground = true, name = "Primary Tabs - Dark")
@Composable
private fun AppPrimaryTabsDarkPreview() {
    var selectedTab by remember { mutableIntStateOf(1) }

    MongezTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.surface)
                .padding(Theme.spacing.lg)
        ) {
            AppPrimaryTabs(
                tabs = listOf("Materials", "Tasks"),
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    }
}