package com.iti.mongez.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.iti.mongez.designsystem.foundation.color.AppColorScheme
import com.iti.mongez.designsystem.foundation.color.DarkColorScheme
import com.iti.mongez.designsystem.foundation.color.LightColorScheme
import com.iti.mongez.designsystem.foundation.elevation.AppElevation
import com.iti.mongez.designsystem.foundation.motion.AppMotion
import com.iti.mongez.designsystem.foundation.radius.AppRadius
import com.iti.mongez.designsystem.foundation.spacing.AppSpacing
import com.iti.mongez.designsystem.foundation.typography.AppTypography
import com.iti.mongez.designsystem.foundation.typography.defaultAppTypography

// ──────────────────────────────────────────────────────────────
// CompositionLocals
// ──────────────────────────────────────────────────────────────
val LocalAppColorScheme = staticCompositionLocalOf { LightColorScheme }
val LocalAppTypography = staticCompositionLocalOf { defaultAppTypography() }
val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }
val LocalAppRadius = staticCompositionLocalOf { AppRadius() }
val LocalAppElevation = staticCompositionLocalOf { AppElevation() }
val LocalAppMotion = staticCompositionLocalOf { AppMotion() }

// ──────────────────────────────────────────────────────────────
// Theme Composable
// ──────────────────────────────────────────────────────────────

/**
 * Root theme wrapper for the Mongez application.
 *
 * Provides all design tokens via [CompositionLocalProvider] and
 * applies a Material 3 [MaterialTheme] underneath for interop with
 * Material components.
 */
@Composable
fun MongezTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val appColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appTypography = defaultAppTypography()

    // Bridge to Material 3 for components that still read MaterialTheme
    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = appColorScheme.brand.primary,
            onPrimary = appColorScheme.brand.onPrimary,
            primaryContainer = appColorScheme.brand.primaryContainer,
            onPrimaryContainer = appColorScheme.brand.onPrimaryContainer,
            background = appColorScheme.surface.background,
            surface = appColorScheme.surface.surface,
            error = appColorScheme.state.error,
            onBackground = appColorScheme.text.primary,
            onSurface = appColorScheme.text.primary,
        )
    } else {
        lightColorScheme(
            primary = appColorScheme.brand.primary,
            onPrimary = appColorScheme.brand.onPrimary,
            primaryContainer = appColorScheme.brand.primaryContainer,
            onPrimaryContainer = appColorScheme.brand.onPrimaryContainer,
            background = appColorScheme.surface.background,
            surface = appColorScheme.surface.surface,
            error = appColorScheme.state.error,
            onBackground = appColorScheme.text.primary,
            onSurface = appColorScheme.text.primary,
        )
    }

    // Status bar styling
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalAppColorScheme provides appColorScheme,
        LocalAppTypography provides appTypography,
        LocalAppSpacing provides AppSpacing(),
        LocalAppRadius provides AppRadius(),
        LocalAppElevation provides AppElevation(),
        LocalAppMotion provides AppMotion(),
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content,
        )
    }
}

// ──────────────────────────────────────────────────────────────
// Theme Accessor Object
// ──────────────────────────────────────────────────────────────

/**
 * Single entry-point for all design tokens.
 *
 * Usage:
 * ```
 * Theme.colorScheme.brand.primary
 * Theme.typography.title.large
 * Theme.spacing.lg
 * Theme.radius.xl
 * Theme.elevation.md
 * Theme.motion.duration.normal
 * ```
 */
object Theme {
    val colorScheme: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val spacing: AppSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalAppSpacing.current

    val radius: AppRadius
        @Composable
        @ReadOnlyComposable
        get() = LocalAppRadius.current

    val elevation: AppElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalAppElevation.current

    val motion: AppMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalAppMotion.current
}
