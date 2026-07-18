package com.iti.mongez.presentation.onboarding.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@Composable
fun OnboardingSkipButton(
    isVisible: Boolean,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.spacing.lg),
        horizontalArrangement = Arrangement.End
    ) {
        if (isVisible) {
            AppButton(
                text = stringResource(id = R.string.onboarding_skip),
                variant = AppButtonVariant.Text,
                fullWidth = false,
                onClick = onSkipClick
            )
        }
    }
}
