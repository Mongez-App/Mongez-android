package com.iti.mongez.presentation.preferences.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.preferences.contract.PreferencesIntent

@Composable
fun SyncCalendarStep(onIntent: (PreferencesIntent) -> Unit) {
    SyncCalendarView(
        onSyncClick = { onIntent(PreferencesIntent.OnSyncCalendarClicked) },
        onSkipClick = { onIntent(PreferencesIntent.OnSkipClicked) },
        modifier = Modifier.padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.xl)
    )
}
