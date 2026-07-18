package com.iti.mongez.designsystem.foundation.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class BrandColors(
    val primary: Color,
    val primaryVariant: Color,
    val primaryContainer: Color,
    val onPrimary: Color,
    val onPrimaryContainer: Color,
    val indicatorUnselected: Color,
)

@Immutable
data class TextColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val hint: Color,
    val disabled: Color,
    val inverse: Color,
)

@Immutable
data class SurfaceColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceContainer: Color,
    val surfaceHigh: Color,
    val surfaceHighest: Color,
)

@Immutable
data class BorderColors(
    val primary: Color,
    val secondary: Color,
    val focused: Color,
    val error: Color,
    val success: Color,
    val disabled: Color,
)

@Immutable
data class StateColors(
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val error: Color,
    val errorContainer: Color,
    val info: Color,
    val infoContainer: Color,
)

@Immutable
data class ButtonColors(
    val primaryBackground: Color,
    val primaryContent: Color,
    val secondaryBackground: Color,
    val secondaryContent: Color,
    val secondaryBorder: Color,
    val disabledBackground: Color,
    val disabledContent: Color,
)

@Immutable
data class InputColors(
    val background: Color,
    val text: Color,
    val placeholder: Color,
    val border: Color,
    val focusedBorder: Color,
    val errorBorder: Color,
    val icon: Color,
)

@Immutable
data class ChipColors(
    val selectedBackground: Color,
    val selectedContent: Color,
    val unselectedBackground: Color,
    val unselectedContent: Color,
    val unselectedBorder: Color,
)

@Immutable
data class NavigationColors(
    val background: Color,
    val activeIcon: Color,
    val activeLabel: Color,
    val inactiveIcon: Color,
    val inactiveLabel: Color,
    val indicator: Color,
)

@Immutable
data class CardColors(
    val background: Color,
    val border: Color,
)

@Immutable
data class AppColorScheme(
    val brand: BrandColors,
    val text: TextColors,
    val surface: SurfaceColors,
    val border: BorderColors,
    val state: StateColors,
    val button: ButtonColors,
    val input: InputColors,
    val chip: ChipColors,
    val navigation: NavigationColors,
    val card: CardColors,
)

// ──────────────────────────────────────────────────────────────
// Light Theme
// ──────────────────────────────────────────────────────────────
val LightColorScheme = AppColorScheme(
    brand = BrandColors(
        primary = PrimitiveColors.Purple600,
        primaryVariant = PrimitiveColors.Purple700,
        primaryContainer = PrimitiveColors.Purple100,
        onPrimary = PrimitiveColors.White,
        onPrimaryContainer = PrimitiveColors.Purple900,
        indicatorUnselected = PrimitiveColors.Gray250,
    ),
    text = TextColors(
        primary = PrimitiveColors.Gray900,
        secondary = PrimitiveColors.Gray600,
        tertiary = PrimitiveColors.Gray500,
        hint = PrimitiveColors.Gray500,
        disabled = PrimitiveColors.Gray400,
        inverse = PrimitiveColors.White,
    ),
    surface = SurfaceColors(
        background = PrimitiveColors.White,
        surface = PrimitiveColors.White,
        surfaceVariant = PrimitiveColors.Gray50,
        surfaceContainer = PrimitiveColors.Gray100,
        surfaceHigh = PrimitiveColors.Gray200,
        surfaceHighest = PrimitiveColors.Gray300,
    ),
    border = BorderColors(
        primary = PrimitiveColors.Gray300,
        secondary = PrimitiveColors.Gray200,
        focused = PrimitiveColors.Purple600,
        error = PrimitiveColors.Red600,
        success = PrimitiveColors.Green600,
        disabled = PrimitiveColors.Gray300,
    ),
    state = StateColors(
        success = PrimitiveColors.Green600,
        successContainer = PrimitiveColors.Green50,
        warning = PrimitiveColors.Orange500,
        warningContainer = PrimitiveColors.Orange50,
        error = PrimitiveColors.Red600,
        errorContainer = PrimitiveColors.Red50,
        info = PrimitiveColors.Blue500,
        infoContainer = PrimitiveColors.Blue50,
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.Purple600,
        primaryContent = PrimitiveColors.White,
        secondaryBackground = PrimitiveColors.White,
        secondaryContent = PrimitiveColors.Gray900,
        secondaryBorder = PrimitiveColors.Gray300,
        disabledBackground = PrimitiveColors.Gray200,
        disabledContent = PrimitiveColors.Gray400,
    ),
    input = InputColors(
        background = PrimitiveColors.Gray50,
        text = PrimitiveColors.Gray900,
        placeholder = PrimitiveColors.Gray500,
        border = PrimitiveColors.Gray300,
        focusedBorder = PrimitiveColors.Purple600,
        errorBorder = PrimitiveColors.Red600,
        icon = PrimitiveColors.Gray500,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.Purple600,
        selectedContent = PrimitiveColors.White,
        unselectedBackground = PrimitiveColors.White,
        unselectedContent = PrimitiveColors.Gray700,
        unselectedBorder = PrimitiveColors.Gray300,
    ),
    navigation = NavigationColors(
        background = PrimitiveColors.White,
        activeIcon = PrimitiveColors.Purple600,
        activeLabel = PrimitiveColors.Purple600,
        inactiveIcon = PrimitiveColors.Gray500,
        inactiveLabel = PrimitiveColors.Gray500,
        indicator = PrimitiveColors.Purple100,
    ),
    card = CardColors(
        background = PrimitiveColors.White,
        border = PrimitiveColors.Gray200,
    ),
)

// ──────────────────────────────────────────────────────────────
// Dark Theme
// ──────────────────────────────────────────────────────────────
private val DarkBackground = Color(0xFF111317)
private val DarkSurface = Color(0xFF181A20)
private val DarkCard = Color(0xFF20232B)

val DarkColorScheme = AppColorScheme(
    brand = BrandColors(
        primary = PrimitiveColors.Purple400,
        primaryVariant = PrimitiveColors.Purple500,
        primaryContainer = PrimitiveColors.Purple900,
        onPrimary = PrimitiveColors.White,
        onPrimaryContainer = PrimitiveColors.Purple200,
        indicatorUnselected = PrimitiveColors.Gray700,
    ),
    text = TextColors(
        primary = PrimitiveColors.White,
        secondary = PrimitiveColors.Gray300,
        tertiary = PrimitiveColors.Gray400,
        hint = PrimitiveColors.Gray500,
        disabled = PrimitiveColors.Gray600,
        inverse = PrimitiveColors.Gray900,
    ),
    surface = SurfaceColors(
        background = DarkBackground,
        surface = DarkSurface,
        surfaceVariant = DarkCard,
        surfaceContainer = DarkCard,
        surfaceHigh = Color(0xFF282B33),
        surfaceHighest = Color(0xFF303340),
    ),
    border = BorderColors(
        primary = PrimitiveColors.Gray700,
        secondary = PrimitiveColors.Gray800,
        focused = PrimitiveColors.Purple400,
        error = PrimitiveColors.Red500,
        success = PrimitiveColors.Green500,
        disabled = PrimitiveColors.Gray700,
    ),
    state = StateColors(
        success = PrimitiveColors.Green500,
        successContainer = Color(0xFF0D2818),
        warning = PrimitiveColors.Orange400,
        warningContainer = Color(0xFF2D1F0E),
        error = PrimitiveColors.Red500,
        errorContainer = Color(0xFF2D1212),
        info = PrimitiveColors.Blue400,
        infoContainer = Color(0xFF0E1D2D),
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.Purple500,
        primaryContent = PrimitiveColors.White,
        secondaryBackground = DarkSurface,
        secondaryContent = PrimitiveColors.White,
        secondaryBorder = PrimitiveColors.Gray700,
        disabledBackground = PrimitiveColors.Gray800,
        disabledContent = PrimitiveColors.Gray600,
    ),
    input = InputColors(
        background = DarkCard,
        text = PrimitiveColors.White,
        placeholder = PrimitiveColors.Gray500,
        border = PrimitiveColors.Gray700,
        focusedBorder = PrimitiveColors.Purple400,
        errorBorder = PrimitiveColors.Red500,
        icon = PrimitiveColors.Gray500,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.Purple500,
        selectedContent = PrimitiveColors.White,
        unselectedBackground = DarkSurface,
        unselectedContent = PrimitiveColors.Gray300,
        unselectedBorder = PrimitiveColors.Gray700,
    ),
    navigation = NavigationColors(
        background = DarkSurface,
        activeIcon = PrimitiveColors.Purple400,
        activeLabel = PrimitiveColors.Purple400,
        inactiveIcon = PrimitiveColors.Gray500,
        inactiveLabel = PrimitiveColors.Gray500,
        indicator = PrimitiveColors.Purple900,
    ),
    card = CardColors(
        background = DarkCard,
        border = PrimitiveColors.Gray700,
    ),
)
