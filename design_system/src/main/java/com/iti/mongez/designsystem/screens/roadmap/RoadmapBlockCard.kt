package com.iti.mongez.designsystem.screens.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun RoadmapBlockCard(
    courseName: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Theme.radius.lg),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.md),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = courseName,
                style = Theme.typography.label.medium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
