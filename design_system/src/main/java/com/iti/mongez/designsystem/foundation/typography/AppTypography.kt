package com.iti.mongez.designsystem.foundation.typography

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class DisplayTypography(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)

@Immutable
data class HeadlineTypography(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)

@Immutable
data class TitleTypography(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)

@Immutable
data class BodyTypography(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)

@Immutable
data class LabelTypography(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
    val extraSmall: TextStyle,
)

@Immutable
data class AppTypography(
    val display: DisplayTypography,
    val headline: HeadlineTypography,
    val title: TitleTypography,
    val body: BodyTypography,
    val label: LabelTypography,
)

fun defaultAppTypography(fontFamily: androidx.compose.ui.text.font.FontFamily = PoppinsFontFamily): AppTypography {
    return AppTypography(
        display = DisplayTypography(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                lineHeight = 48.sp,
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 44.sp,
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
            ),
        ),
        headline = HeadlineTypography(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            ),
        ),
        title = TitleTypography(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 26.sp,
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
        ),
        body = BodyTypography(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
        ),
        label = LabelTypography(
            large = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
            medium = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            ),
            small = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            ),
            extraSmall = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                lineHeight = 14.sp,
            ),
        ),
    )
}
