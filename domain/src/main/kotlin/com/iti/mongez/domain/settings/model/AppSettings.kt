package com.iti.mongez.domain.settings.model

enum class Language(val code: String) {
    SYSTEM("system"),
    EN("en"),
    AR("ar")
}

data class AppSettings(
    val isCalendarSyncEnabled: Boolean = false,
    val isDarkModeEnabled: Boolean = false,
    val language: Language = Language.EN
)
