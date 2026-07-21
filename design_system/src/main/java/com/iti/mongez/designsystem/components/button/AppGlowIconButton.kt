package com.iti.mongez.designsystem.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun AppGlowIconButton(
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color,
    glowColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(Theme.spacing.huge)
            .shadow(
                elevation = Theme.elevation.xl,
                shape = CircleShape,
                spotColor = glowColor,
                ambientColor = glowColor.copy(alpha = 0.5f)
            )
            .clip(CircleShape)
            .background(Theme.colorScheme.surface.background)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(Theme.spacing.xl)
        )
    }
}

@Composable
fun AppGlowIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color,
    glowColor: Color
) {
    AppGlowIconButton(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        iconTint = iconTint,
        glowColor = glowColor
    )
}
