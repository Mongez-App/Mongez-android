package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@Composable
fun BottomActions(
    activeCount: Int,
    onReset: () -> Unit,
    onApply: () -> Unit
) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Theme.spacing.lg, start = Theme.spacing.lg, end = Theme.spacing.lg)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppButton(
                text = stringResource(R.string.filter_reset),
                onClick = onReset,
                variant = AppButtonVariant.Text,
                fullWidth = false
            )

            val applyText = if (activeCount > 0) {
                stringResource(R.string.filter_apply_count, activeCount)
            } else {
                stringResource(R.string.filter_apply)
            }

            AppButton(
                text = applyText,
                onClick = onApply,
                fullWidth = false
            )
        }
}