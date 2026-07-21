package com.iti.mongez.presentation.main

import com.iti.mongez.presentation.R

enum class MainTab(
    val labelResId: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    Home(
        R.string.nav_home,
        R.drawable.home,
        R.drawable.home
    ),

    Courses(
        R.string.nav_courses,
        R.drawable.courses,
        R.drawable.courses
    ),

    Roadmap(
        R.string.nav_roadmap,
        R.drawable.roadmap,
        R.drawable.roadmap
    ),

    Profile(
        R.string.nav_profile,
        R.drawable.profile,
        R.drawable.profile
    );

    companion object {
        fun fromIndex(index: Int): MainTab =
            entries.getOrElse(index) { Home }
    }
}