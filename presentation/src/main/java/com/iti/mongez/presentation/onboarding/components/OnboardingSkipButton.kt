package com.iti.mongez.presentation.onboarding.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun OnboardingSkipButton(
    isVisible: Boolean,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Theme.spacing.md),
        horizontalArrangement = Arrangement.End
    ) {
        if (isVisible) {
            AppButton(
                text = "Skip",
                variant = AppButtonVariant.Text,
                fullWidth = false,
                onClick = onSkipClick
            )
        }
    }
}
