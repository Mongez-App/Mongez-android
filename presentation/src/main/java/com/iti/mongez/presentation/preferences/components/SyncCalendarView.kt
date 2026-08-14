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
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun SyncCalendarView(
    onSyncClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = Theme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.sync_calendar),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.preferences_sync_calendar_title),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        Text(
            text = stringResource(id = R.string.preferences_sync_calendar_subtitle),
            style = Theme.typography.body.large,
            color = Theme.colorScheme.text.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = Theme.spacing.xl)
        )

        Spacer(modifier = Modifier.weight(1.5f))

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

@Preview(showBackground = true)
@Composable
private fun SyncCalendarViewPreview() {
    MongezTheme {
        SyncCalendarView(
            onSyncClick = {},
            onSkipClick = {}
        )
    }
}

