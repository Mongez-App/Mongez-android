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
            .padding(horizontal = Theme.spacing.lg)
            .padding(bottom = Theme.spacing.xl),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
    ) {
        if (!isFirstPage) {
            AppButton(
                text = stringResource(id = R.string.onboarding_previous),
                variant = AppButtonVariant.Secondary,
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            )
        }

        if (isLastPage) {
            AppButton(
                text = stringResource(id = R.string.onboarding_get_started),
                onClick = onGetStartedClick,
                modifier = if (isFirstPage) Modifier.fillMaxWidth() else Modifier.weight(1f)
            )
        } else {
            AppButton(
                text = stringResource(id = R.string.onboarding_next),
                onClick = onNextClick,
                modifier = if (isFirstPage) Modifier.fillMaxWidth() else Modifier.weight(1f)
            )
        }
    }
}
