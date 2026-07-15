package com.iti.mongez.designsystem.foundation.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.runtime.Immutable

@Immutable
data class MotionDuration(
    val instant: Int = 0,
    val fast: Int = 150,
    val normal: Int = 250,
    val slow: Int = 350,
    val extraSlow: Int = 500,
)

@Immutable
data class MotionEasing(
    val linear: Easing = CubicBezierEasing(0f, 0f, 1f, 1f),
    val easeOut: Easing = CubicBezierEasing(0f, 0f, 0.2f, 1f),
    val fastOutSlowIn: Easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f),
    val linearOutSlowIn: Easing = CubicBezierEasing(0f, 0f, 0.2f, 1f),
    val easeInOut: Easing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f),
)

@Immutable
data class AppMotion(
    val duration: MotionDuration = MotionDuration(),
    val easing: MotionEasing = MotionEasing(),
)
