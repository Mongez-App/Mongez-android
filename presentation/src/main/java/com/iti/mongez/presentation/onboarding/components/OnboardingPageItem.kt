package com.iti.mongez.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.onboarding.uiState.OnboardingPage

@Composable
fun OnboardingPageItem(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Theme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (page.imageRes != null) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(240.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(Theme.colorScheme.surface.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Illustration",
                    style = Theme.typography.body.medium,
                    color = Theme.colorScheme.text.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.giant))

        Text(
            text = page.title,
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        Text(
            text = page.description,
            style = Theme.typography.body.large,
            color = Theme.colorScheme.text.secondary,
            textAlign = TextAlign.Center
        )
    }
}
