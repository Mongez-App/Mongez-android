package com.iti.mongez.designsystem.components.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.progress.AppCircularProgressIndicator
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Full-screen centered loading indicator.
 */
@Composable
fun AppCircularLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AppCircularProgressIndicator()
    }
}

/**
 * Shimmer loading placeholder block.
 *
 * @param modifier Modifier.
 * @param height Height of the shimmer block.
 */
@Composable
fun AppShimmer(
    modifier: Modifier = Modifier,
    height: Dp = 80.dp,
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer_translate",
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Theme.colorScheme.surface.surfaceHigh,
            Theme.colorScheme.surface.surfaceVariant,
            Theme.colorScheme.surface.surfaceHigh,
        ),
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(Theme.radius.md))
            .background(shimmerBrush),
    )
}

@Preview(showBackground = true, name = "Shimmer Loading")
@Composable
private fun ShimmerPreview() {
    MongezTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            AppShimmer(height = 120.dp)
            Spacer(modifier = Modifier.height(12.dp))
            AppShimmer(height = 20.dp)
            Spacer(modifier = Modifier.height(8.dp))
            AppShimmer(height = 20.dp, modifier = Modifier.fillMaxWidth(0.6f))
        }
    }
}
