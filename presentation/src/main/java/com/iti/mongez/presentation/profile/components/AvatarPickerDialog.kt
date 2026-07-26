package com.iti.mongez.presentation.profile.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.domain.utils.AvatarConstants
import com.iti.mongez.presentation.R

@Composable
fun AvatarPickerDialog(
    selectedAvatarUrl: String?,
    onAvatarSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Theme.radius.dialog),
            color = Theme.colorScheme.surface.background,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.md)
        ) {
            Column(
                modifier = Modifier.padding(Theme.spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.choose_avatar_title),
                    style = Theme.typography.title.large,
                    color = Theme.colorScheme.text.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.height(300.dp),
                    contentPadding = PaddingValues(Theme.spacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                ) {
                    items(AvatarConstants.AVATARS) { avatarUrl ->
                        val isSelected = avatarUrl == selectedAvatarUrl
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) Theme.colorScheme.brand.primary else androidx.compose.ui.graphics.Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { onAvatarSelected(avatarUrl) },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = avatarUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                androidx.compose.material3.TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(com.iti.mongez.designsystem.R.string.action_cancel),
                        color = Theme.colorScheme.brand.primary
                    )
                }
            }
        }
    }
}
