package com.iti.mongez.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.iti.mongez.presentation.R

enum class MainTab(
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Home(
        R.string.nav_home,
        Icons.Filled.Home,
        Icons.Outlined.Home
    ),

    Courses(
        R.string.nav_courses,
        Icons.AutoMirrored.Filled.MenuBook,
        Icons.AutoMirrored.Outlined.MenuBook
    ),

    Roadmap(
        R.string.nav_roadmap,
        Icons.Filled.CalendarMonth,
        Icons.Outlined.CalendarMonth
    ),

    Profile(
        R.string.nav_profile,
        Icons.Filled.Person,
        Icons.Outlined.Person
    );

    companion object {
        fun fromIndex(index: Int): MainTab =
            entries.getOrElse(index) { Home }
    }
}