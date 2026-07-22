package com.iti.mongez.designsystem.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iti.mongez.designsystem.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (TimePickerState) -> Unit,
    state: TimePickerState
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = RoundedCornerShape(Theme.radius.dialog),
            color = Theme.colorScheme.surface.background,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = RoundedCornerShape(Theme.radius.dialog),
                    color = Theme.colorScheme.surface.background
                ),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(Theme.spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    style = Theme.typography.label.medium,
                    color = Theme.colorScheme.text.secondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Theme.spacing.lg)
                )
                
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Theme.colorScheme.surface.surfaceLow,
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Theme.colorScheme.text.primary,
                        selectorColor = Theme.colorScheme.brand.primary,
                        periodSelectorBorderColor = Theme.colorScheme.brand.primary,
                        periodSelectorSelectedContainerColor = Theme.colorScheme.brand.primaryContainer,
                        periodSelectorUnselectedContainerColor = Theme.colorScheme.surface.surfaceLow,
                        periodSelectorSelectedContentColor = Theme.colorScheme.brand.primary,
                        periodSelectorUnselectedContentColor = Theme.colorScheme.text.secondary,
                        timeSelectorSelectedContainerColor = Theme.colorScheme.brand.primaryContainer,
                        timeSelectorUnselectedContainerColor = Theme.colorScheme.surface.surfaceLow,
                        timeSelectorSelectedContentColor = Theme.colorScheme.brand.primary,
                        timeSelectorUnselectedContentColor = Theme.colorScheme.text.primary
                    )
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Theme.spacing.lg),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel", color = Theme.colorScheme.text.secondary)
                    }
                    TextButton(onClick = { onConfirm(state) }) {
                        Text("OK", color = Theme.colorScheme.brand.primary)
                    }
                }
            }
        }
    }
}
