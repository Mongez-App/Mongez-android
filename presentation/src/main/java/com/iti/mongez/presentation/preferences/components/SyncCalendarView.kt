package com.iti.mongez.presentation.preferences.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@Composable
fun SyncCalendarView(
    onSyncClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_calendar),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xxxl))

        Text(
            text = stringResource(id = R.string.preferences_sync_calendar_title),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Text(
            text = stringResource(id = R.string.preferences_sync_calendar_subtitle),
            style = Theme.typography.body.large,
            color = Theme.colorScheme.text.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Theme.spacing.xl)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.giant))

        AppButton(
            text = stringResource(id = R.string.preferences_sync_google_calendar),
            onClick = onSyncClick,
            variant = AppButtonVariant.Primary
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppButton(
            text = stringResource(id = R.string.preferences_skip_for_now),
            onClick = onSkipClick,
            variant = AppButtonVariant.Text
        )
    }
}
