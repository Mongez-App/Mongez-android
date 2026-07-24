package com.iti.mongez.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
    profilePictureUrl: String?,
    onEditClick: () -> Unit
) {
    android.util.Log.d("ProfileHeader", "Displaying Avatar: $profilePictureUrl")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
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
                    .background(Theme.colorScheme.surface.surfaceLow)
                    .clickable { onEditClick() },
                contentAlignment = Alignment.Center
            ) {
                val personPainter = rememberVectorPainter(Icons.Rounded.Person)
                val isUrlValid = !profilePictureUrl.isNullOrBlank() && profilePictureUrl != "null"
                
                if (isUrlValid) {
                    AsyncImage(
                        model = profilePictureUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = personPainter,
                        error = personPainter
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

            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(Theme.spacing.xl + Theme.spacing.sm)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Theme.colorScheme.brand.primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "Edit Profile",
                    modifier = Modifier.size(Theme.spacing.md)
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { onEditClick() }
        ) {
            Text(
                text = name,
                style = Theme.typography.title.large,
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(Theme.spacing.xs))
            Icon(
                imageVector = Icons.Rounded.Edit,
                contentDescription = null,
                tint = Theme.colorScheme.brand.primary,
                modifier = Modifier.size(Theme.spacing.md)
            )
        }

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
            profilePictureUrl = null,
            onEditClick = {}
        )
    }
}
