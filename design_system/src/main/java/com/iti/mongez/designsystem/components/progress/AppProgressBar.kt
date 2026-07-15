package com.iti.mongez.designsystem.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Linear progress bar with rounded corners.
 *
 * @param progress Progress value between 0f and 1f.
 * @param modifier Modifier.
 * @param height Bar height.
 */
@Composable
fun AppLinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(Theme.motion.duration.extraSlow),
        label = "progress",
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(Theme.radius.full)),
        color = Theme.colorScheme.brand.primary,
        trackColor = Theme.colorScheme.surface.surfaceHigh,
    )
}

/**
 * Circular progress indicator.
 *
 * @param modifier Modifier.
 * @param progress Optional determinate progress (0f..1f). If null, indeterminate.
 * @param size Diameter of the indicator.
 * @param strokeWidth Width of the arc stroke.
 */
@Composable
fun AppCircularProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    size: Dp = 48.dp,
    strokeWidth: Dp = 4.dp,
) {
    if (progress != null) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = tween(Theme.motion.duration.extraSlow),
            label = "circular_progress",
        )
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = modifier.size(size),
            color = Theme.colorScheme.brand.primary,
            trackColor = Theme.colorScheme.surface.surfaceHigh,
            strokeWidth = strokeWidth,
        )
    } else {
        CircularProgressIndicator(
            modifier = modifier.size(size),
            color = Theme.colorScheme.brand.primary,
            trackColor = Theme.colorScheme.surface.surfaceHigh,
            strokeWidth = strokeWidth,
        )
    }
}

@Preview(showBackground = true, name = "Linear Progress")
@Composable
private fun LinearProgressPreview() {
    MongezTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            AppLinearProgressBar(progress = 0.7f)
            Spacer(modifier = Modifier.height(16.dp))
            AppLinearProgressBar(progress = 0.34f)
        }
    }
}

@Preview(showBackground = true, name = "Circular Progress")
@Composable
private fun CircularProgressPreview() {
    MongezTheme {
        AppCircularProgressIndicator(
            progress = 0.6f,
            modifier = Modifier.padding(16.dp),
        )
    }
}
