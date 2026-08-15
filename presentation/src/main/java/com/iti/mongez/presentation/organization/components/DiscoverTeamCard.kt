package com.iti.mongez.presentation.organization.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import kotlin.math.absoluteValue

@Composable
fun DiscoverTeamCard(
    teamName: String,
    organizationName: String,
    imageUrl: String?,
    appliedDate: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initials = remember(teamName) {
        teamName.split(" ").take(2).joinToString("") { it.take(1) }.uppercase()
    }
    
    val avatarColors = listOf(
        Color(0xFFA855F7), // Purple
        Color(0xFF3B82F6), // Blue
        Color(0xFFF59E0B), // Orange
        Color(0xFF10B981)  // Green
    )
    val avatarColor = remember(teamName) {
        avatarColors[teamName.hashCode().absoluteValue % avatarColors.size]
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = Theme.colorScheme.border.primary,
                shape = RoundedCornerShape(Theme.radius.md)
            )
            .clip(RoundedCornerShape(Theme.radius.md))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Theme.radius.md),
        colors = CardDefaults.cardColors(
            containerColor = Theme.colorScheme.surface.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Theme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            if (imageUrl.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(Theme.radius.sm))
                        .background(avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        color = Color.White,
                        style = Theme.typography.headline.small,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "$teamName logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(Theme.radius.sm))
                )
            }

            Spacer(modifier = Modifier.width(Theme.spacing.md))

            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = teamName,
                    style = Theme.typography.body.large,
                    fontWeight = FontWeight.SemiBold,
                    color = Theme.colorScheme.text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = organizationName,
                    style = Theme.typography.body.small,
                    color = Theme.colorScheme.text.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Applied Date
            if (appliedDate != null) {
                Spacer(modifier = Modifier.width(Theme.spacing.sm))
                Text(
                    text = appliedDate,
                    style = Theme.typography.label.small,
                    color = Theme.colorScheme.text.tertiary
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun DiscoverTeamCardPreview() {
    MongezTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DiscoverTeamCard(
                teamName = "Java and Mobile 101",
                organizationName = "Information Technology Institute",
                imageUrl = null,
                appliedDate = "Applied May 25",
                onClick = {}
            )
            DiscoverTeamCard(
                teamName = "Web Development Track",
                organizationName = "Google Developer Groups",
                imageUrl = null,
                onClick = {}
            )
        }
    }
}
