package com.iti.mongez.designsystem.foundation.typography

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.iti.mongez.designsystem.R

/**
 * Font families used throughout the application.
 *
 * - **Poppins**: Primary English font
 * - **IBM Plex Sans Arabic**: Arabic font
 * - **Madimi One**: Application logo font
 */
val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),
)

val IbmPlexArabicFontFamily = FontFamily(
    Font(R.font.ibm_plex_sans_arabic_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_arabic_medium, FontWeight.Medium),
    Font(R.font.ibm_plex_sans_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.ibm_plex_sans_arabic_bold, FontWeight.Bold),
)

val MadimiOneFontFamily = FontFamily(
    Font(R.font.madimi_one_regular, FontWeight.Normal),
)

/**
 * Combined font family that uses Poppins for Latin (English) characters
 * and IBM Plex Sans Arabic for Arabic characters.
 */
val MongezFontFamily = FontFamily(
    // Latin fonts (Poppins) - Listed first to take precedence for Latin characters
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold),

    // Arabic fonts (IBM Plex) - Used for Arabic characters
    Font(R.font.ibm_plex_sans_arabic_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_sans_arabic_medium, FontWeight.Medium),
    Font(R.font.ibm_plex_sans_arabic_semibold, FontWeight.SemiBold),
    Font(R.font.ibm_plex_sans_arabic_bold, FontWeight.Bold),
)
