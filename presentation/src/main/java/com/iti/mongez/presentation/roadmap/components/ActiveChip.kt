package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun ActiveChip(text: String, onClear: () -> Unit) {
    InputChip(
        selected = true,
        onClick = onClear,
        label = { Text(text, style = Theme.typography.label.small) },
        trailingIcon = {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Remove $text filter",
                modifier = Modifier.size(16.dp)
            )
        },
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = Theme.colorScheme.brand.primaryContainer,
            selectedLabelColor = Theme.colorScheme.brand.primary,
            selectedTrailingIconColor = Theme.colorScheme.brand.primary
        )
    )
}