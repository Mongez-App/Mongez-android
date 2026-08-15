package com.iti.mongez.presentation.main

import com.iti.mongez.presentation.R

enum class MainTab(
    val labelResId: Int,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    Home(
        R.string.nav_home,
        R.drawable.ic_home_filled,
        R.drawable.ic_home_unfilled
    ),

    Courses(
        R.string.nav_courses,
        R.drawable.ic_courses_filled,
        R.drawable.ic_courses_unfilled
    ),

    Roadmap(
        R.string.nav_roadmap,
        R.drawable.ic_roadmap_filled,
        R.drawable.ic_roadmap_unfilled
    ),
    Track(
        R.string.add_event_create, // Ensure this string resource exists (e.g., "Track")
        R.drawable.ic_courses_filled, // Replace with your track icon
        R.drawable.ic_courses_unfilled
    ),

    Profile(
        R.string.nav_profile,
        R.drawable.ic_profile_filled,
        R.drawable.ic_profile_unfilled
    );

    companion object {
        fun fromIndex(index: Int): MainTab =
            entries.getOrElse(index) { Home }
    }
}