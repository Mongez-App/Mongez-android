package com.iti.mongez.presentation.preferences.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.preferences.contract.PreferencesIntent
import com.iti.mongez.presentation.preferences.uiState.PreferencesUiState

@Composable
fun AvailableDaysStep(state: PreferencesUiState, onIntent: (PreferencesIntent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.xxl))

        Text(
            text = stringResource(id = R.string.preferences_available_days_title),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Text(
            text = stringResource(id = R.string.preferences_available_days_subtitle),
            style = Theme.typography.body.large,
            color = Theme.colorScheme.text.tertiary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xxxl))

        DaysSelectionGrid(
            selectedDays = state.selectedDays,
            onDayToggle = { onIntent(PreferencesIntent.OnDaySelected(it)) }
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))
    }
}

@Preview(showBackground = true)
@Composable
private fun AvailableDaysStepPreview() {
    MongezTheme {
        AvailableDaysStep(
            state = PreferencesUiState(selectedDays = setOf("Mon", "Tue", "Wed")),
            onIntent = {}
        )
    }
}

