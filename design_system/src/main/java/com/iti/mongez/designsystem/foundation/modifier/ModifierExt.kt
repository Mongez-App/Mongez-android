package com.iti.mongez.designsystem.foundation.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies a custom blur-based shadow to a component.
 * Useful for matching Figma "Drop Shadow" effects which are more Gaussian than Material elevation.
 *
 * @param color The color of the shadow, including alpha.
 * @param borderRadius The corner radius of the shadow.
 * @param blurRadius The blur radius (matching Figma's "Blur" value).
 * @param offsetY Vertical offset of the shadow.
 * @param offsetX Horizontal offset of the shadow.
 * @param spread How much to expand the shadow size before blurring.
 */
fun Modifier.mongezShadow(
    color: Color,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
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
