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
    val roadmapPurple: Color,
    val roadmapGreen: Color,
    val roadmapOrange: Color,
    val roadmapBlue: Color,
    val roadmapTimeline: Color,
)

@Immutable
data class TextColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val hint: Color,
    val disabled: Color,
    val inverse: Color,
    val dialogLabel: Color,
)

@Immutable
data class SurfaceColors(
    val background: Color,
    val surface: Color,
    val surfaceLow: Color,
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
data class EventColors(
    val studyContainer: Color,
    val studyIcon: Color,
    val assignmentContainer: Color,
    val assignmentIcon: Color,
    val quizContainer: Color,
    val quizIcon: Color,
    val examContainer: Color,
    val examIcon: Color,
    val projectContainer: Color,
    val projectIcon: Color,
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
    val events: EventColors,
)

// ──────────────────────────────────────────────────────────────
// Light Theme
// ──────────────────────────────────────────────────────────────
val LightColorScheme = AppColorScheme(
    brand = BrandColors(
        primary = PrimitiveColors.Purple200,
        primaryVariant = PrimitiveColors.Purple500,
        primaryContainer = PrimitiveColors.Purple200.copy(alpha = 0.15F),
        onPrimary = Color.White,
        onPrimaryContainer = PrimitiveColors.Black100,
        roadmapPurple = PrimitiveColors.Purple200,
        roadmapGreen = PrimitiveColors.Green600,
        roadmapOrange = PrimitiveColors.Yellow50,
        roadmapBlue = PrimitiveColors.Blue500,
        roadmapTimeline = PrimitiveColors.Gray100,
    ),
    text = TextColors(
        primary = PrimitiveColors.Black100,
        secondary = PrimitiveColors.Gray200,
        tertiary = PrimitiveColors.Gray300,
        hint = PrimitiveColors.Gray300,
        disabled = PrimitiveColors.Gray200,
        inverse = Color.White,
        dialogLabel = PrimitiveColors.Gray200,
    ),
    surface = SurfaceColors(
        background = PrimitiveColors.White100,
        surface = Color.White,
        surfaceLow = PrimitiveColors.White100,
        surfaceVariant = PrimitiveColors.Gray50,
        surfaceContainer = PrimitiveColors.Gray50,
        surfaceHigh = PrimitiveColors.Gray200,
        surfaceHighest = PrimitiveColors.Gray300,
    ),
    border = BorderColors(
        primary = PrimitiveColors.Gray100,
        secondary = PrimitiveColors.Gray200,
        focused = PrimitiveColors.Purple200,
        error = PrimitiveColors.Red600,
        success = PrimitiveColors.Green600,
        disabled = PrimitiveColors.Gray300,
    ),
    state = StateColors(
        success = PrimitiveColors.Green600,
        successContainer = PrimitiveColors.Green600.copy(alpha = 0.1F),
        warning = PrimitiveColors.Yellow50,
        warningContainer = PrimitiveColors.Yellow50.copy(alpha = 0.1F),
        error = PrimitiveColors.Red600,
        errorContainer = PrimitiveColors.Red600.copy(alpha = 0.1F),
        info = PrimitiveColors.Blue500,
        infoContainer = PrimitiveColors.Blue500.copy(alpha = 0.1F),
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.Purple200,
        primaryContent = Color.White,
        secondaryBackground = PrimitiveColors.White100,
        secondaryContent = PrimitiveColors.Black100,
        secondaryBorder = PrimitiveColors.Gray100,
        disabledBackground = PrimitiveColors.Gray200,
        disabledContent = PrimitiveColors.Gray300,
    ),
    input = InputColors(
        background = PrimitiveColors.White100,
        text = PrimitiveColors.Black100,
        placeholder = PrimitiveColors.Gray300,
        border = PrimitiveColors.Gray100,
        focusedBorder = PrimitiveColors.Purple200,
        errorBorder = PrimitiveColors.Red600,
        icon = PrimitiveColors.Gray300,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.Purple200,
        selectedContent = Color.White,
        unselectedBackground = Color.White,
        unselectedContent = PrimitiveColors.Gray300,
        unselectedBorder = PrimitiveColors.Gray100,
    ),
    navigation = NavigationColors(
        background = PrimitiveColors.White100,
        activeIcon = PrimitiveColors.Purple200,
        activeLabel = PrimitiveColors.Purple200,
        inactiveIcon = PrimitiveColors.Gray300,
        inactiveLabel = PrimitiveColors.Gray200,
        indicator = PrimitiveColors.Gray100,
    ),
    card = CardColors(
        background = PrimitiveColors.White100,
        border = PrimitiveColors.Gray100,
    ),
    events = EventColors(
        studyContainer = PrimitiveColors.Purple500.copy(alpha = 0.2F),
        studyIcon = PrimitiveColors.Purple500,
        assignmentContainer = PrimitiveColors.Red600.copy(alpha = 0.2F),
        assignmentIcon = PrimitiveColors.Red600,
        quizContainer = PrimitiveColors.Green600.copy(alpha = 0.2F),
        quizIcon = PrimitiveColors.Green600,
        examContainer = PrimitiveColors.Yellow50.copy(alpha = 0.2F),
        examIcon = PrimitiveColors.Yellow50,
        projectContainer = PrimitiveColors.Blue500.copy(alpha = 0.2F),
        projectIcon = PrimitiveColors.Blue500,
    ),
)

// ──────────────────────────────────────────────────────────────
// Dark Theme
// ──────────────────────────────────────────────────────────────
val DarkColorScheme = AppColorScheme(
    brand = BrandColors(
        primary = PrimitiveColors.Purple400,
        primaryVariant = PrimitiveColors.Purple50,
        primaryContainer = PrimitiveColors.Purple400.copy(alpha = 0.15F),
        onPrimary = Color.White,
        onPrimaryContainer = PrimitiveColors.Gray420,
        roadmapPurple = PrimitiveColors.Purple400,
        roadmapGreen = PrimitiveColors.Green600,
        roadmapOrange = PrimitiveColors.Orange100,
        roadmapBlue = PrimitiveColors.Blue200,
        roadmapTimeline = PrimitiveColors.Gray350,
    ),
    text = TextColors(
        primary = PrimitiveColors.Gray420,
        secondary = PrimitiveColors.Purple350,
        tertiary = PrimitiveColors.Purple350,
        hint = PrimitiveColors.Purple350,
        disabled = PrimitiveColors.Gray350,
        inverse = PrimitiveColors.Black100,
        dialogLabel = PrimitiveColors.Purple350,
    ),
    surface = SurfaceColors(
        background = PrimitiveColors.BackgroundDark,
        surface = PrimitiveColors.BackgroundDark,
        surfaceLow = PrimitiveColors.BackgroundDark,
        surfaceVariant = PrimitiveColors.Gray350,
        surfaceContainer = PrimitiveColors.Gray350,
        surfaceHigh = PrimitiveColors.Purple350,
        surfaceHighest = PrimitiveColors.Purple350,
    ),
    border = BorderColors(
        primary = PrimitiveColors.Gray350,
        secondary = PrimitiveColors.Purple350,
        focused = PrimitiveColors.Purple400,
        error = PrimitiveColors.Red600,
        success = PrimitiveColors.Green600,
        disabled = PrimitiveColors.Purple350,
    ),
    state = StateColors(
        success = PrimitiveColors.Green600,
        successContainer = PrimitiveColors.Green600.copy(alpha = 0.15F),
        warning = PrimitiveColors.Orange100,
        warningContainer = PrimitiveColors.Orange100.copy(alpha = 0.15F),
        error = PrimitiveColors.Red600,
        errorContainer = PrimitiveColors.Yellow50.copy(alpha = 0.15F),
        info = PrimitiveColors.Blue200,
        infoContainer = PrimitiveColors.Blue200.copy(alpha = 0.15F),
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.Purple400,
        primaryContent = Color.White,
        secondaryBackground = PrimitiveColors.BackgroundDark,
        secondaryContent = Color.White,
        secondaryBorder = PrimitiveColors.Gray350,
        disabledBackground = PrimitiveColors.Purple350,
        disabledContent = PrimitiveColors.Gray350,
    ),
    input = InputColors(
        background = PrimitiveColors.BackgroundDark,
        text = Color.White,
        placeholder = PrimitiveColors.Purple350,
        border = PrimitiveColors.Gray350,
        focusedBorder = PrimitiveColors.Purple400,
        errorBorder = PrimitiveColors.Red600,
        icon = PrimitiveColors.Purple350,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.Purple400,
        selectedContent = Color.White,
        unselectedBackground = PrimitiveColors.BackgroundDark,
        unselectedContent = PrimitiveColors.Purple350,
        unselectedBorder = PrimitiveColors.Gray350,
    ),
    navigation = NavigationColors(
        background = PrimitiveColors.BackgroundDark,
        activeIcon = PrimitiveColors.Purple400,
        activeLabel = PrimitiveColors.Purple400,
        inactiveIcon = PrimitiveColors.Purple350,
        inactiveLabel = PrimitiveColors.Purple350,
        indicator = PrimitiveColors.Gray350,
    ),
    card = CardColors(
        background = PrimitiveColors.BackgroundDark,
        border = PrimitiveColors.Gray350,
    ),
    events = EventColors(
        studyContainer = PrimitiveColors.Purple50.copy(alpha = 0.2F),
        studyIcon = PrimitiveColors.Purple50,
        assignmentContainer = PrimitiveColors.Red600.copy(alpha = 0.2F),
        assignmentIcon = PrimitiveColors.Red600,
        quizContainer = PrimitiveColors.Green600.copy(alpha = 0.2F),
        quizIcon = PrimitiveColors.Green600,
        examContainer = PrimitiveColors.Yellow50.copy(alpha = 0.2F),
        examIcon = PrimitiveColors.Yellow50,
        projectContainer = PrimitiveColors.Blue200.copy(alpha = 0.2F),
        projectIcon = PrimitiveColors.Blue200,
    ),
)
