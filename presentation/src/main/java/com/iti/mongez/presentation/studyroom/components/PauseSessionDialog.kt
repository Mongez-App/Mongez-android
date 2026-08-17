package com.iti.mongez.presentation.studyroom.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun PauseSessionDialog(
    onDismiss: () -> Unit,
    onPauseSession: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Theme.radius.xl),
            color = Theme.colorScheme.surface.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.md)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Theme.spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(Theme.spacing.giant)
                        .background(Theme.colorScheme.state.error.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = "Warning",
                        tint = Theme.colorScheme.state.error,
                        modifier = Modifier.size(Theme.spacing.xxl)
                    )
                }
                
                Spacer(modifier = Modifier.height(Theme.spacing.xl))
                
                Text(
                    text = "Pause this session?",
                    style = Theme.typography.title.large.copy(fontWeight = FontWeight.Bold),
                    color = Theme.colorScheme.text.primary
                )
                
                Spacer(modifier = Modifier.height(Theme.spacing.md))
                
                Text(
                    text = "Your progress will be saved but this task will NOT be marked as complete.",
                    style = Theme.typography.body.medium,
                    color = Theme.colorScheme.text.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = Theme.spacing.md)
                )
                
                Spacer(modifier = Modifier.height(Theme.spacing.xxl))
                
                Button(
                    onClick = onPauseSession,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Theme.spacing.huge + Theme.spacing.sm),
                    shape = RoundedCornerShape(Theme.radius.md),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Theme.colorScheme.state.error
                    )
                ) {
                    Text(
                        "Pause session", 
                        color = Theme.colorScheme.surface.background,
                        style = Theme.typography.label.large.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                
                Spacer(modifier = Modifier.height(Theme.spacing.md))
                
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Theme.spacing.huge + Theme.spacing.sm),
                    shape = RoundedCornerShape(Theme.radius.md),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Theme.colorScheme.border.primary),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Theme.colorScheme.text.primary
                    )
                ) {
                    Text(
                        "Keep Studying",
                        style = Theme.typography.label.large.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}
