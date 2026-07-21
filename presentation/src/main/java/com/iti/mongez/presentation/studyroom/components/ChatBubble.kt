package com.iti.mongez.presentation.studyroom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun ChatBubble(text: String, isUser: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(
                    RoundedCornerShape(
                        topStart = Theme.radius.md,
                        topEnd = Theme.radius.md,
                        bottomStart = if (isUser) Theme.radius.md else Theme.radius.xs,
                        bottomEnd = if (isUser) Theme.radius.xs else Theme.radius.md
                    )
                )
                .background(
                    if (isUser) Theme.colorScheme.brand.primary.copy(alpha = 0.5f)
                    else Theme.colorScheme.surface.surfaceHigh
                )
                .padding(Theme.spacing.md)
        ) {
            Text(
                text = text,
                color = Theme.colorScheme.text.primary,
                style = Theme.typography.body.medium
            )
        }
    }
}
