package com.iti.mongez.designsystem.foundation.elevation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppElevation(
    val none: Dp = 0.dp,
    val xs: Dp = 1.dp,
    val sm: Dp = 2.dp,
    val md: Dp = 4.dp,
    val lg: Dp = 8.dp,
    val xl: Dp = 12.dp,
)
