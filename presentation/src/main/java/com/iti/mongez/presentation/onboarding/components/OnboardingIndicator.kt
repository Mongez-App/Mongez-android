package com.iti.mongez.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.lerp
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import kotlin.math.absoluteValue

@Composable
fun OnboardingIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Theme.spacing.md),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { index ->
            val pageOffset = (pagerState.currentPage - index + pagerState.currentPageOffsetFraction).absoluteValue
            val fraction = 1f - pageOffset.coerceIn(0f, 1f)

            val width = lerp(Theme.spacing.sm, Theme.spacing.xl, fraction)
            val color = lerp(Theme.colorScheme.brand.indicatorUnselected, Theme.colorScheme.brand.primary, fraction)

            Box(
                modifier = Modifier
                    .padding(horizontal = Theme.spacing.xs)
                    .size(width = width, height = Theme.spacing.sm)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingIndicatorPreview() {
    MongezTheme {
        val pagerState = rememberPagerState(pageCount = { 3 })
        OnboardingIndicator(pagerState = pagerState)
    }
}

