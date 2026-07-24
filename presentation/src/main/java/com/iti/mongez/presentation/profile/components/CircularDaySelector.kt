package com.iti.mongez.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun CircularDaySelector(
    selectedDays: Set<String>,
    onDayToggle: (String) -> Unit
) {
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val dayLabels = listOf("S", "M", "T", "W", "T", "F", "S")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEachIndexed { index, day ->
            val isSelected = selectedDays.contains(day)
            Box(
                modifier = Modifier
                    .size(Theme.spacing.huge)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Theme.colorScheme.brand.primary 
                        else Theme.colorScheme.surface.surfaceLow
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Transparent
                                else Theme.colorScheme.border.primary.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDayToggle(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayLabels[index],
                    style = Theme.typography.label.large,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color.White else Theme.colorScheme.text.primary
                )
            }
        }
    }
}