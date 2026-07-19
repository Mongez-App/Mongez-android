package com.iti.mongez.designsystem.components.chip

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Chip for filters, day selection, categories, and tags.
 *
 * @param label Chip text.
 * @param selected Whether the chip is in selected state.
 * @param onSelectedChange Callback when selection toggles.
 * @param modifier Modifier.
 * @param enabled Whether the chip is interactive.
 */
@Composable
fun AppChip(
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedContainerColor: Color = Theme.colorScheme.chip.selectedBackground,
    selectedLabelColor: Color = Theme.colorScheme.chip.selectedContent,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) selectedContainerColor
        else Theme.colorScheme.chip.unselectedBackground,
        animationSpec = tween(Theme.motion.duration.fast),
        label = "chip_bg",
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) selectedLabelColor
        else Theme.colorScheme.chip.unselectedContent,
        animationSpec = tween(Theme.motion.duration.fast),
        label = "chip_content",
    )

    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        label = {
            Text(
                text = label,
                style = Theme.typography.label.medium,
            )
        },
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(Theme.radius.sm),
        border = if (!selected) {
            BorderStroke(1.dp, Theme.colorScheme.chip.unselectedBorder)
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = backgroundColor,
            labelColor = contentColor,
            selectedContainerColor = backgroundColor,
            selectedLabelColor = contentColor,
            disabledContainerColor = Theme.colorScheme.button.disabledBackground,
            disabledLabelColor = Theme.colorScheme.button.disabledContent,
        ),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true, name = "Chips")
@Composable
private fun ChipPreview() {
    MongezTheme {
        FlowRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AppChip(label = "Mon", selected = true, onSelectedChange = {})
            AppChip(label = "Tue", selected = false, onSelectedChange = {})
            AppChip(label = "Wed", selected = true, onSelectedChange = {})
            AppChip(label = "Thu", selected = false, onSelectedChange = {})
            AppChip(label = "Fri", selected = true, onSelectedChange = {})
        }
    }
}
