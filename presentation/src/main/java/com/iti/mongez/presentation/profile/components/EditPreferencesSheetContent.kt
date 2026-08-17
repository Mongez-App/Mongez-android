package com.iti.mongez.presentation.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.preferences.components.StudyHoursPicker

@Composable
fun EditPreferencesSheetContent(
    selectedHours: Int,
    selectedDays: Set<String>,
    onHoursChanged: (Int) -> Unit,
    onDayToggle: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isLoading: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Theme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.edit_preferences_title),
            style = Theme.typography.title.large,
            fontWeight = FontWeight.Bold,
            color = Theme.colorScheme.text.primary,
            modifier = Modifier.padding(bottom = Theme.spacing.xl)
        )

        Text(
            text = stringResource(R.string.preferences_study_hours_title),
            style = Theme.typography.title.small,
            fontWeight = FontWeight.Bold,
            color = Theme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Theme.spacing.lg),
            textAlign = TextAlign.Center
        )

        StudyHoursPicker(
            selectedHours = selectedHours,
            onHoursChanged = onHoursChanged,
            modifier = Modifier
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Text(
            text = stringResource(R.string.preferences_available_days_title),
            style = Theme.typography.title.small,
            fontWeight = FontWeight.Bold,
            color = Theme.colorScheme.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Theme.spacing.lg)
        )

        CircularDaySelector(
            selectedDays = selectedDays,
            onDayToggle = onDayToggle
        )

        Spacer(modifier = Modifier.height(Theme.spacing.giant))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            AppButton(
                text = stringResource(id = R.string.action_cancel),
                onClick = onCancel,
                variant = AppButtonVariant.Secondary,
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = stringResource(R.string.save_button),
                onClick = onSave,
                variant = AppButtonVariant.Primary,
                modifier = Modifier.weight(1f),
                isLoading = isLoading,
                enabled = selectedDays.isNotEmpty()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditPreferencesSheetContentPreview() {
    MongezTheme {
        EditPreferencesSheetContent(
            selectedHours = 4,
            selectedDays = setOf("Mon", "Wed", "Fri"),
            onHoursChanged = {},
            onDayToggle = {},
            onSave = {},
            onCancel = {}
        )
    }
}

