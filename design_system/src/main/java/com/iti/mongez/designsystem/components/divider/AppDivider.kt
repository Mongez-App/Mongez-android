package com.iti.mongez.designsystem.components.divider

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme

/**
 * Standardized horizontal divider.
 *
 * @param modifier Modifier.
 * @param startIndent Start indentation.
 */
@Composable
fun AppDivider(
    modifier: Modifier = Modifier,
    startIndent: Dp = 0.dp,
) {
    HorizontalDivider(
        modifier = modifier.padding(start = startIndent),
        thickness = 1.dp,
        color = Theme.colorScheme.border.secondary,
    )
}
