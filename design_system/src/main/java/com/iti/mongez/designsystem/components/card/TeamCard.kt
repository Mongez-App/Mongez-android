package com.iti.mongez.designsystem.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iti.mongez.designsystem.components.progress.AppLinearProgressBar
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun TeamCard(
    teamName: String,
    photoUrl: String?,
    progress: Int,
    organizationName: String? = null,
    eventsCount: Int = 0,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    AppCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = Theme.elevation.sm
    ) {
        Column(
            modifier = Modifier.padding(Theme.spacing.md)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Team image
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "$teamName logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(Theme.spacing.md))
                
                Column {
                    Text(
                        text = teamName,
                        style = Theme.typography.title.medium,
                        fontWeight = FontWeight.Bold,
                        color = Theme.colorScheme.text.primary
                    )
                    
                    if (organizationName != null) {
                        Text(
                            text = organizationName,
                            style = Theme.typography.body.small,
                            color = Theme.colorScheme.text.secondary
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Theme.spacing.md))
            
            // Progress Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Team Progress",
                    style = Theme.typography.label.medium,
                    color = Theme.colorScheme.text.secondary
                )
                Text(
                    text = "$progress%",
                    style = Theme.typography.label.medium,
                    fontWeight = FontWeight.Bold,
                    color = Theme.colorScheme.brand.primary
                )
            }
            
            Spacer(modifier = Modifier.height(Theme.spacing.xs))
            
            AppLinearProgressBar(
                progress = progress / 100f,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(Theme.spacing.md))
            
            // Events Tag (Simulated with text for now, can use AppChip if available)
            val eventsText = if (eventsCount > 0) "$eventsCount events this week" else "No events this week"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Theme.radius.sm))
                    .background(Theme.colorScheme.surface.surfaceVariant)
                    .padding(horizontal = Theme.spacing.sm, vertical = Theme.spacing.xs)
            ) {
                Text(
                    text = eventsText,
                    style = Theme.typography.label.small,
                    color = Theme.colorScheme.text.secondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamCardPreview() {
    MongezTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TeamCard(
                teamName = "Mobile Native",
                photoUrl = null,
                progress = 25,
                organizationName = "Information Technology Institute",
                eventsCount = 5
            )
        }
    }
}
