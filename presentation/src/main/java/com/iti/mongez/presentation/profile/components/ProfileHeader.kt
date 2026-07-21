package com.iti.mongez.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.components.button.appShadow
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun ProfileHeader(
    name: String,
    email: String,
    profilePictureUrl: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(Theme.spacing.huge * 2)
                .appShadow(
                    color = Theme.colorScheme.brand.primary.copy(alpha = 0.75f),
                    borderRadius = Theme.spacing.huge,
                    blurRadius = Theme.spacing.lg,
                    spread = -Theme.spacing.xs,
                    offsetX = 0.dp,
                    offsetY = 0.dp
                )
                .clip(CircleShape)
                .background(Theme.colorScheme.surface.surfaceLow),
            contentAlignment = Alignment.Center
        ) {
            if (profilePictureUrl != null) {
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier.size(Theme.spacing.huge),
                    tint = Theme.colorScheme.text.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        Text(
            text = name,
            style = Theme.typography.title.large,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = email,
            style = Theme.typography.body.medium,
            color = Theme.colorScheme.text.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    MongezTheme {
        ProfileHeader(
            name = "Abdullah Mohamed",
            email = "abdullah@example.com",
            profilePictureUrl = null
        )
    }
}