package com.iti.mongez.designsystem.components.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

fun Modifier.appShadow(
    color: Color = Color(0xFF5B4FE9).copy(alpha = 0.3f),
    borderRadius: Dp = 14.dp,
    blurRadius: Dp = 20.dp,
    offsetY: Dp = 10.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = drawBehind {
    drawIntoCanvas { canvas ->
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        if (blurRadius != 0.dp) {
            frameworkPaint.maskFilter = (android.graphics.BlurMaskFilter(
                blurRadius.toPx(),
                android.graphics.BlurMaskFilter.Blur.NORMAL
            ))
        }
        frameworkPaint.color = color.toArgb()

        val leftPixel = offsetX.toPx() - spread.toPx()
        val topPixel = offsetY.toPx() - spread.toPx()
        val rightPixel = size.width + offsetX.toPx() + spread.toPx()
        val bottomPixel = size.height + offsetY.toPx() + spread.toPx()

        canvas.drawRoundRect(
            left = leftPixel,
            top = topPixel,
            right = rightPixel,
            bottom = bottomPixel,
            radiusX = borderRadius.toPx(),
            radiusY = borderRadius.toPx(),
            paint = paint
        )
    }
}

enum class AppButtonVariant {
    Primary,
    Secondary,
    Outlined,
    Text,
}

/**
 * Standard button component for the Mongez application.
 *
 * @param text Button label.
 * @param onClick Callback invoked on click.
 * @param modifier Modifier for external layout adjustments.
 * @param variant Visual style ([AppButtonVariant]).
 * @param enabled Whether the button is interactive.
 * @param isLoading When `true`, shows a spinner instead of text.
 * @param leadingIcon Optional icon displayed before the text.
 * @param fullWidth Whether the button spans the full available width.
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    variant: AppButtonVariant = AppButtonVariant.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    fullWidth: Boolean = true,
) {
    val shape = RoundedCornerShape(Theme.radius.md)
    val heightModifier = modifier
        .height(48.dp)
        .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)

    when (variant) {
        AppButtonVariant.Primary -> {
            val bgColor by animateColorAsState(
                targetValue = when {
                    !enabled -> Theme.colorScheme.button.disabledBackground
                    else -> Theme.colorScheme.button.primaryBackground
                },
                animationSpec = tween(Theme.motion.duration.fast),
                label = "primary_bg",
            )
            val contentColor = when {
                !enabled -> Theme.colorScheme.button.disabledContent
                else -> Theme.colorScheme.button.primaryContent
            }

            Button(
                onClick = onClick,
                modifier = heightModifier.then(innerModifier),
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = bgColor,
                    contentColor = contentColor,
                    disabledContainerColor = Theme.colorScheme.button.disabledBackground,
                    disabledContentColor = Theme.colorScheme.button.disabledContent,
                ),
                contentPadding = PaddingValues(horizontal = Theme.spacing.lg),
            ) {
                ButtonContent(
                    text = text,
                    isLoading = isLoading,
                    contentColor = contentColor,
                    leadingIcon = leadingIcon,
                )
            }
        }

        AppButtonVariant.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = heightModifier,
                enabled = enabled && !isLoading,
                shape = shape,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (enabled) Theme.colorScheme.button.secondaryBorder
                    else Theme.colorScheme.button.disabledContent,
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Theme.colorScheme.button.secondaryBackground,
                    contentColor = Theme.colorScheme.button.secondaryContent,
                    disabledContentColor = Theme.colorScheme.button.disabledContent,
                ),
                contentPadding = PaddingValues(horizontal = Theme.spacing.lg),
            ) {
                ButtonContent(
                    text = text,
                    isLoading = isLoading,
                    contentColor = if (enabled) Theme.colorScheme.button.secondaryContent
                    else Theme.colorScheme.button.disabledContent,
                    leadingIcon = leadingIcon,
                )
            }
        }

        AppButtonVariant.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                modifier = heightModifier,
                enabled = enabled && !isLoading,
                shape = shape,
                border = BorderStroke(
                    width = 1.5.dp,
                    color = if (enabled) Theme.colorScheme.brand.primary
                    else Theme.colorScheme.button.disabledContent,
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Theme.colorScheme.brand.primary,
                    disabledContentColor = Theme.colorScheme.button.disabledContent,
                ),
                contentPadding = PaddingValues(horizontal = Theme.spacing.lg),
            ) {
                ButtonContent(
                    text = text,
                    isLoading = isLoading,
                    contentColor = if (enabled) Theme.colorScheme.brand.primary
                    else Theme.colorScheme.button.disabledContent,
                    leadingIcon = leadingIcon,
                )
            }
        }

        AppButtonVariant.Text -> {
            TextButton(
                onClick = onClick,
                modifier = heightModifier,
                enabled = enabled && !isLoading,
                shape = shape,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Theme.colorScheme.brand.primary,
                    disabledContentColor = Theme.colorScheme.button.disabledContent,
                ),
                contentPadding = PaddingValues(horizontal = Theme.spacing.lg),
            ) {
                ButtonContent(
                    text = text,
                    isLoading = isLoading,
                    contentColor = if (enabled) Theme.colorScheme.brand.primary
                    else Theme.colorScheme.button.disabledContent,
                    leadingIcon = leadingIcon,
                )
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    isLoading: Boolean,
    contentColor: Color,
    leadingIcon: ImageVector?,
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = contentColor,
            strokeWidth = 2.5.dp,
        )
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = contentColor,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = Theme.typography.label.large,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Primary Button")
@Composable
private fun PrimaryButtonPreview() {
    MongezTheme {
        AppButton(text = "Continue", onClick = {})
    }
}

@Preview(showBackground = true, name = "Secondary Button")
@Composable
private fun SecondaryButtonPreview() {
    MongezTheme {
        AppButton(text = "Cancel", onClick = {}, variant = AppButtonVariant.Secondary)
    }
}

@Preview(showBackground = true, name = "Outlined Button")
@Composable
private fun OutlinedButtonPreview() {
    MongezTheme {
        AppButton(text = "View All", onClick = {}, variant = AppButtonVariant.Outlined)
    }
}

@Preview(showBackground = true, name = "Text Button")
@Composable
private fun TextButtonPreview() {
    MongezTheme {
        AppButton(text = "Skip", onClick = {}, variant = AppButtonVariant.Text)
    }
}

@Preview(showBackground = true, name = "Loading Button")
@Composable
private fun LoadingButtonPreview() {
    MongezTheme {
        AppButton(text = "Saving...", onClick = {}, isLoading = true)
    }
}

@Preview(showBackground = true, name = "Disabled Button")
@Composable
private fun DisabledButtonPreview() {
    MongezTheme {
        AppButton(text = "Disabled", onClick = {}, enabled = false)
    }
}
