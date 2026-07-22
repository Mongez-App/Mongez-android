package com.iti.mongez.designsystem.screens.roadmap

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun RoadmapBlockCard(
    courseName: String,
    color: Color,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    isExpandable: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .let { 
                if (onClick != null) it.clickable { onClick() } else it 
            },
        shape = RoundedCornerShape(Theme.radius.lg),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = courseName,
                style = Theme.typography.label.medium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

            if (isExpandable) {
                Icon(
                    imageVector = if (isExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Roadmap Block Card - Light")
@Composable
private fun RoadmapBlockCardPreview() {
    MongezTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            RoadmapBlockCard(
                courseName = "Android Native Development",
                color = Theme.colorScheme.brand.roadmapPurple,
                isExpandable = true,
                isExpanded = false
            )

            RoadmapBlockCard(
                courseName = "Clean Architecture Patterns",
                color = Theme.colorScheme.brand.roadmapGreen,
                isExpandable = true,
                isExpanded = true
            )
        }
    }
}

@Preview(showBackground = true, name = "Roadmap Block Card - Dark")
@Composable
private fun RoadmapBlockCardDarkPreview() {
    MongezTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            RoadmapBlockCard(
                courseName = "Data Structures & Algorithms",
                color = Theme.colorScheme.brand.roadmapBlue,
                isExpandable = false
            )

            RoadmapBlockCard(
                courseName = "UI/UX Design Principles",
                color = Theme.colorScheme.brand.roadmapOrange,
                isExpandable = true,
                isExpanded = false
            )
        }
    }
}
