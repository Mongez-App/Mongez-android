package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun BottomActions(
    activeCount: Int,
    onReset: () -> Unit,
    onApply: () -> Unit
) {
    Surface(
        color = Theme.colorScheme.surface.background,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.lg)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onReset) {
                Text("Reset", color = Theme.colorScheme.text.secondary)
            }
            
            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(containerColor = Theme.colorScheme.brand.primary)
            ) {
                Text("Apply ${if (activeCount > 0) "($activeCount)" else ""}")
            }
        }
    }
}