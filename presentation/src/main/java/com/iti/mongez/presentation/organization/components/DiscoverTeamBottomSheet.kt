package com.iti.mongez.presentation.organization.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.theme.Theme
import kotlin.math.absoluteValue

@Composable
fun DiscoverTeamBottomSheet(
    teamName: String,
    organizationName: String?,
    imageUrl: String?,
    status: String?,
    isLoading: Boolean,
    onJoinClick: (String) -> Unit
) {
    val initials = remember(teamName) {
        teamName.split(" ").take(2).joinToString("") { it.take(1) }.uppercase()
    }

    val avatarColors = listOf(
        Color(0xFF10B981), // Green
        Color(0xFFA855F7), // Purple
        Color(0xFF3B82F6), // Blue
        Color(0xFFF59E0B)  // Orange
    )
    val avatarColor = remember(teamName) {
        avatarColors[teamName.hashCode().absoluteValue % avatarColors.size]
    }

    var inviteCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        if (imageUrl.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = Color.White,
                    style = Theme.typography.headline.large,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = "$teamName logo",
                modifier = Modifier
                    .size(96.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Team Name
        Text(
            text = teamName,
            style = Theme.typography.headline.medium,
            fontWeight = FontWeight.Bold,
            color = Theme.colorScheme.text.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Organization Name
        Text(
            text = organizationName ?: "Unknown Organization",
            style = Theme.typography.body.medium,
            color = Theme.colorScheme.text.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Status Badge
        if (!status.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .background(
                        color = Theme.colorScheme.brand.primary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(Theme.radius.full)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = status.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                    style = Theme.typography.label.medium,
                    color = Theme.colorScheme.brand.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Invite Code Input
        AppTextField(
            value = inviteCode,
            onValueChange = { inviteCode = it },
            placeholder = "Enter Invite Code or Team ID",
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (inviteCode.isNotBlank()) {
                            onJoinClick(inviteCode)
                        }
                    },
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Theme.colorScheme.brand.primary)
                        .size(40.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "Join Team",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
