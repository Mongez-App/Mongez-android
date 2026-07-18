package com.iti.mongez.designsystem.components.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.R
import com.iti.mongez.designsystem.theme.Theme

/**
 * A reusable skip button typically used in onboarding or preference flows.
 *
 * @param isVisible Controls the visibility with a fade animation.
 * @param onSkipClick Callback when the button is clicked.
 * @param modifier Modifier for the container.
 * @param text The text to display, defaults to "Skip".
 */
@Composable
fun AppSkipButton(
    isVisible: Boolean,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = stringResource(id = R.string.app_skip)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.lg)
            .height(56.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AppButton(
                text = text,
                variant = AppButtonVariant.Text,
                fullWidth = false,
                onClick = onSkipClick
            )
        }
    }
}
