package com.iti.mongez.presentation.onboarding.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun OnboardingActions(
    isFirstPage: Boolean,
    isLastPage: Boolean,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.xl)
            .padding(bottom = Theme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
    ) {
        if (!isFirstPage) {
            AppButton(
                text = "Previous",
                variant = AppButtonVariant.Secondary,
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            )
        }

        if (isLastPage) {
            AppButton(
                text = "Get Started",
                onClick = onGetStartedClick,
                modifier = if (isFirstPage) Modifier.fillMaxWidth() else Modifier.weight(1f)
            )
        } else {
            AppButton(
                text = "Next",
                onClick = onNextClick,
                modifier = if (isFirstPage) Modifier.fillMaxWidth() else Modifier.weight(1f)
            )
        }
    }
}
