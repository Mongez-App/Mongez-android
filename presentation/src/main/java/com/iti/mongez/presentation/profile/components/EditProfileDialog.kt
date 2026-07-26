package com.iti.mongez.presentation.profile.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@Composable
fun EditProfileDialog(
    name: String,
    selectedAvatarUrl: String?,
    profilePictureUrl: String?,
    onNameChange: (String) -> Unit,
    onImageClick: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Theme.radius.dialog),
            color = Theme.colorScheme.surface.background,
            tonalElevation = 6.dp,
            modifier = Modifier.width(340.dp)
        ) {
            Column(
                modifier = Modifier.padding(Theme.spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.edit_profile_title),
                    style = Theme.typography.title.large,
                    color = Theme.colorScheme.text.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Theme.colorScheme.surface.surfaceLow)
                        .clickable { onImageClick() },
                    contentAlignment = Alignment.Center
                ) {
                    val model = selectedAvatarUrl ?: profilePictureUrl
                    val isModelValid = model != null && model.isNotBlank() && model != "null"
                    
                    if (isModelValid) {
                        AsyncImage(
                            model = model,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    // Overlay icon
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.CameraAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(Theme.spacing.xl)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                AppTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = stringResource(R.string.label_display_name),
                    placeholder = stringResource(R.string.hint_display_name)
                )

                Spacer(modifier = Modifier.height(Theme.spacing.xxl))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                ) {
                    AppButton(
                        text = stringResource(com.iti.mongez.designsystem.R.string.action_cancel),
                        onClick = onDismiss,
                        variant = AppButtonVariant.Secondary,
                        modifier = Modifier.weight(1f)
                    )
                    AppButton(
                        text = stringResource(R.string.save_button),
                        onClick = onSave,
                        variant = AppButtonVariant.Primary,
                        modifier = Modifier.weight(1f),
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}
