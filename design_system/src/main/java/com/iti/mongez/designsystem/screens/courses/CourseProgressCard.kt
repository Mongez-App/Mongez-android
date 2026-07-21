package com.iti.mongez.designsystem.screens.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun CourseProgressCard(
    completedTasks: Int,
    totalTasks: Int,
    percentage: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(Theme.radius.xxl),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colorScheme.brand.primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.spacing.xl),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Course Progress",
                    color = Theme.colorScheme.brand.onPrimary.copy(alpha = 0.8f),
                    style = Theme.typography.label.medium
                )
                Box(
                    modifier = Modifier
                        .background(Theme.colorScheme.brand.onPrimary.copy(alpha = 0.2f), RoundedCornerShape(Theme.radius.md))
                        .padding(horizontal = Theme.spacing.md, vertical = Theme.spacing.xs),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$percentage%",
                        color = Theme.colorScheme.brand.onPrimary,
                        style = Theme.typography.label.small,
                        fontSize = 11.sp
                    )
                }
            }
            
            Text(
                text = "$completedTasks of $totalTasks tasks done",
                color = Theme.colorScheme.brand.onPrimary,
                style = Theme.typography.title.large.copy(fontWeight = FontWeight.Bold)
            )

            LinearProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Theme.colorScheme.brand.onPrimary,
                trackColor = Theme.colorScheme.brand.onPrimary.copy(alpha = 0.3f)
            )
        }
    }
}
