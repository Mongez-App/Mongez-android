package com.iti.mongez.designsystem.foundation.radius

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppRadius(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 20.dp,
    val xxl: Dp = 24.dp,
    val sheet: Dp = 28.dp,
    val dialog: Dp = 32.dp,
    val background: Dp = 56.dp,
    val full: Dp = 999.dp,
)
