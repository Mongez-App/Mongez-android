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

@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    viewModel: ProfileViewModel,
    onNavigateToLogin: () -> Unit,
    onShowSnackBar: (String) -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.NavigateToLogin -> onNavigateToLogin()
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
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Theme.colorScheme.surface.surfaceLow
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
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Theme.colorScheme.surface.surfaceLow
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
                OutlinedCard(
                    shape = RoundedCornerShape(Theme.radius.md),
                    border = BorderStroke(1.dp, Theme.colorScheme.border.secondary),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = Theme.spacing.sm, vertical = Theme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewState.language,
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

@Composable
private fun ProfileHeader(
    name: String,
    email: String,
    profilePictureUrl: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Theme.colorScheme.surface.surfaceLow),
            contentAlignment = Alignment.Center
        ) {
            if (profilePictureUrl != null) {
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Theme.colorScheme.text.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        Text(
            text = name,
            style = Theme.typography.title.large,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = email,
            style = Theme.typography.body.medium,
            color = Theme.colorScheme.text.secondary
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color
) {
    AppCard(
        modifier = modifier,
        containerColor = Color.Transparent,
        borderWidth = 1.dp,
        elevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = Theme.typography.label.small,
                color = Theme.colorScheme.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(32.dp)
            )
            Spacer(modifier = Modifier.height(Theme.spacing.xs))
            Text(
                text = value,
                style = Theme.typography.title.large,
                color = valueColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    iconContainerColor: Color,
    iconTint: Color,
    title: String,
    titleColor: Color = Theme.colorScheme.text.primary,
    action: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else Modifier
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Theme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Theme.spacing.huge)
                .background(iconContainerColor, RoundedCornerShape(Theme.radius.md)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(Theme.spacing.lg)
            )
        }

        Spacer(modifier = Modifier.width(Theme.spacing.lg))

        Text(
            text = title,
            style = Theme.typography.body.large,
            color = titleColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        if (action != null) {
            action()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    MongezTheme {
        ProfileScreenContent(
            innerPadding = PaddingValues(),
            viewState = ProfileViewState(
                name = "Abdullah Mohamed",
                email = "abdullah@example.com",
                studyingHours = 145,
                completedTasks = 382,
                streakDays = 14,
                isCalendarSyncEnabled = true,
                isDarkModeEnabled = false,
                language = "EN"
            ),
            onIntent = {}
        )
    }
}
