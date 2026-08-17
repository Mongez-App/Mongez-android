package com.iti.mongez.presentation.studyroom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun ChatBubble(text: String, isUser: Boolean, isTyping: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (isUser) Spacer(modifier = Modifier.fillMaxWidth(0.15f))
        
        Box(
            modifier = Modifier
                .weight(1f, fill = false)
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
            if (isTyping) {
                TypingIndicator()
            } else {
                Text(
                    text = text,
                    color = Theme.colorScheme.text.primary,
                    style = Theme.typography.body.medium
                )
            }
        }
        
        if (!isUser) Spacer(modifier = Modifier.fillMaxWidth(0.15f))
    }
}


