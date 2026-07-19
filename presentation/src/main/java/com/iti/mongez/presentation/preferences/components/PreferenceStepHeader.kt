package com.iti.mongez.presentation.preferences.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@Composable
fun PreferenceStepHeader(
    modifier: Modifier = Modifier,
    step: Int,
    totalSteps: Int = 3
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.preferences_step_of_3, step),
            style = Theme.typography.label.large,
            color = Theme.colorScheme.brand.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        Row(
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalSteps) { index ->
                val isSelected = index + 1 == step
                Box(
                    modifier = Modifier
                        .width(if (isSelected) Theme.spacing.xl else Theme.spacing.md)
                        .height(Theme.spacing.xs)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) Theme.colorScheme.brand.primary
                            else Theme.colorScheme.brand.indicatorUnselected
                        )
                )
            }
        }
    }
}
