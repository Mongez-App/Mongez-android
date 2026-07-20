package com.iti.mongez.presentation.roadmap.contract

import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType

sealed class RoadmapEffect {
    data class ShowSnackBar(val message: String, val type: AppSnackbarType) : RoadmapEffect()
    data class NavigateToBlockDetails(val blockId: String) : RoadmapEffect()
    object NavigateToAddEvent : RoadmapEffect()
}
