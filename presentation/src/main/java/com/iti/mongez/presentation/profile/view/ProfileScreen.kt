package com.iti.mongez.presentation.profile.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iti.mongez.designsystem.components.card.AppCard
import com.iti.mongez.designsystem.components.divider.AppDivider
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.profile.contract.ProfileEffect
import com.iti.mongez.presentation.profile.contract.ProfileIntent
import com.iti.mongez.presentation.profile.uiState.ProfileViewState
import com.iti.mongez.presentation.profile.viewmodel.ProfileViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.iti.mongez.domain.settings.model.Language
import com.iti.mongez.presentation.profile.components.ProfileHeader
import com.iti.mongez.presentation.profile.components.SettingItem
import com.iti.mongez.presentation.profile.components.StatCard

@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    viewModel: ProfileViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToPreferences: () -> Unit,
    onShowSnackBar: (String) -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToLogin -> onNavigateToLogin()
                is ProfileEffect.NavigateToPreferences -> onNavigateToPreferences()
                is ProfileEffect.ShowError -> onShowSnackBar(effect.message)
            }
        }
    }

    ProfileScreenContent(
        innerPadding = innerPadding,
        viewState = viewState,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun ProfileScreenContent(
    innerPadding: PaddingValues,
    viewState: ProfileViewState,
    onIntent: (ProfileIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(Theme.spacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Header
        ProfileHeader(
            name = viewState.name,
            email = viewState.email,
            profilePictureUrl = viewState.profilePictureUrl
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xxl))

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.profile_studying_hours),
                value = viewState.studyingHours.toString(),
                valueColor = Theme.colorScheme.brand.primary
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.profile_completed_tasks),
                value = viewState.completedTasks.toString(),
                valueColor = Theme.colorScheme.brand.primary
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.profile_streak_days),
                value = viewState.streakDays.toString(),
                valueColor = Theme.colorScheme.brand.primary
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xxl))

        // Settings Items
        SettingItem(
            icon = Icons.Rounded.CalendarMonth,
            iconContainerColor = Theme.colorScheme.state.infoContainer,
            iconTint = Theme.colorScheme.state.info,
            title = stringResource(R.string.profile_calendar_sync),
            action = {
                Switch(
                    checked = viewState.isCalendarSyncEnabled,
                    onCheckedChange = { onIntent(ProfileIntent.ToggleCalendarSync(it)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Theme.colorScheme.brand.primary,
                        uncheckedThumbColor = Theme.colorScheme.brand.primary,
                        uncheckedTrackColor = Theme.colorScheme.brand.primary.copy(alpha = 0.12f),
                        uncheckedBorderColor = Theme.colorScheme.brand.primary
                    )
                )
            }
        )

        AppDivider(modifier = Modifier.padding(vertical = Theme.spacing.md))

        SettingItem(
            icon = Icons.Rounded.Nightlight,
            iconContainerColor = Theme.colorScheme.surface.surfaceLow,
            iconTint = Theme.colorScheme.text.primary,
            title = stringResource(R.string.profile_dark_mode),
            action = {
                Switch(
                    checked = viewState.isDarkModeEnabled,
                    onCheckedChange = { onIntent(ProfileIntent.ToggleDarkMode(it)) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Theme.colorScheme.brand.primary,
                        uncheckedThumbColor = Theme.colorScheme.brand.primary,
                        uncheckedTrackColor = Theme.colorScheme.brand.primary.copy(alpha = 0.12f),
                        uncheckedBorderColor = Theme.colorScheme.brand.primary
                    )
                )
            }
        )

        AppDivider(modifier = Modifier.padding(vertical = Theme.spacing.md))

        SettingItem(
            icon = Icons.Rounded.Translate,
            iconContainerColor = Theme.colorScheme.brand.primaryContainer,
            iconTint = Theme.colorScheme.brand.primary,
            title = stringResource(R.string.profile_language),
            action = {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedCard(
                        onClick = { expanded = true },
                        shape = RoundedCornerShape(Theme.radius.md),
                        border = BorderStroke(1.dp, Theme.colorScheme.border.secondary),
                        colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = Theme.spacing.sm,
                                vertical = Theme.spacing.xs
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = viewState.language.name,
                                style = Theme.typography.body.small,
                                color = Theme.colorScheme.text.secondary
                            )
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Theme.colorScheme.text.secondary,
                                modifier = Modifier.size(Theme.spacing.md)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Language.entries.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(language.name) },
                                onClick = {
                                    onIntent(ProfileIntent.ChangeLanguage(language))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        )

        AppDivider(modifier = Modifier.padding(vertical = Theme.spacing.md))

        SettingItem(
            icon = Icons.Rounded.Tune,
            iconContainerColor = Theme.colorScheme.state.warningContainer,
            iconTint = Theme.colorScheme.state.warning,
            title = stringResource(R.string.profile_studying_preferences),
            onClick = { onIntent(ProfileIntent.EditPreferences) },
            action = {
                Text(
                    text = stringResource(R.string.profile_edit),
                    style = Theme.typography.body.medium,
                    color = Theme.colorScheme.text.secondary
                )
            }
        )

        AppDivider(modifier = Modifier.padding(vertical = Theme.spacing.md))

        SettingItem(
            icon = Icons.AutoMirrored.Rounded.Logout,
            iconContainerColor = Theme.colorScheme.state.errorContainer,
            iconTint = Theme.colorScheme.state.error,
            title = stringResource(R.string.profile_logout),
            titleColor = Theme.colorScheme.state.error,
            onClick = { onIntent(ProfileIntent.Logout) }
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun ProfileScreenPreview() {
    val sampleState = ProfileViewState(
        name = "Abdullh Mohamed",
        email = "abdullh@example.com",
        studyingHours = 145,
        completedTasks = 382,
        streakDays = 14,
        isCalendarSyncEnabled = true,
        isDarkModeEnabled = false,
        language = Language.EN
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            ProfileScreenContent(
                innerPadding = PaddingValues(),
                viewState = sampleState,
                onIntent = {}
            )
        }
    }
}

@Preview(showBackground = true, locale = "ar", name = "Arabic Preview")
@Composable
private fun ProfileScreenArabicPreview() {
    val sampleState = ProfileViewState(
        name = "عبدالله محمد",
        email = "abdullh@example.com",
        studyingHours = 145,
        completedTasks = 382,
        streakDays = 14,
        isCalendarSyncEnabled = true,
        isDarkModeEnabled = false,
        language = Language.AR
    )

    MongezTheme {
        Surface(color = Theme.colorScheme.surface.background) {
            ProfileScreenContent(
                innerPadding = PaddingValues(),
                viewState = sampleState,
                onIntent = {}
            )
        }
    }
}
