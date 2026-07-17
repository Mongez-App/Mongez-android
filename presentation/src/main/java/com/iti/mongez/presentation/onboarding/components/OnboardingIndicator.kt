package com.iti.mongez.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun OnboardingIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Theme.spacing.xl),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            Box(
                modifier = Modifier
                    .padding(horizontal = Theme.spacing.xs)
                    .size(if (isSelected) Theme.spacing.md else Theme.spacing.sm)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Theme.colorScheme.brand.primary 
                        else Theme.colorScheme.border.secondary
                    )
            )
        }
    }
}
