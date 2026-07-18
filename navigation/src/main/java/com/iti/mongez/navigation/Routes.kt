package com.iti.mongez.navigation

sealed interface AppRoute {
    object Onboarding : AppRoute
    object Login : AppRoute
    object Preferences : AppRoute
    object Dashboard : AppRoute
}
