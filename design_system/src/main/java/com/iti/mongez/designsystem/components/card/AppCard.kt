package com.iti.mongez.designsystem.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Generic reusable card container.
 *
 * @param modifier Modifier for external layout.
 * @param onClick Optional click handler. If null, the card is not clickable.
 * @param elevation Card elevation.
 * @param borderWidth Optional border width. Use 0.dp for no border.
 * @param content Composable content.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: Dp = Theme.elevation.xs,
    borderWidth: Dp = 0.dp,
    containerColor: Color = Theme.colorScheme.card.background,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(Theme.radius.xl)
    val colors = CardDefaults.cardColors(
        containerColor = containerColor,
    )
    val border = if (borderWidth > 0.dp) {
        BorderStroke(borderWidth, Theme.colorScheme.card.border)
    } else null

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            border = border,
        ) {
            Box(modifier = Modifier.padding(Theme.spacing.lg)) {
                content()
            }
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            border = border,
        ) {
            Box(modifier = Modifier.padding(Theme.spacing.lg)) {
                content()
            }
        }
    }
}

@Preview(showBackground = true, name = "Card")
@Composable
private fun CardPreview() {
    MongezTheme {
        AppCard(
            modifier = Modifier.padding(16.dp),
            onClick = {},
        ) {
            Column {
                Text("Card Title", style = Theme.typography.title.medium)
                Text("Card body content", style = Theme.typography.body.medium)
            }
        }
    }
}
