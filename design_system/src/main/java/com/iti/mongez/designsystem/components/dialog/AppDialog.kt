package com.iti.mongez.designsystem.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.button.appShadow
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Confirmation dialog with title, description, and two actions.
 *
 * @param title Dialog title.
 * @param description Dialog body text.
 * @param primaryActionText Primary button text.
 * @param onPrimaryAction Primary action callback.
 * @param onDismiss Dismiss callback.
 * @param secondaryActionText Optional secondary button text.
 * @param onSecondaryAction Optional secondary action callback.
 * @param illustration Optional composable slot for an illustration/icon.
 * @param content Optional extra content slot (e.g. for showing default selection summary).
 */
@Composable
fun AppConfirmationDialog(
    title: String,
    description: String,
    primaryActionText: String,
    onPrimaryAction: () -> Unit,
    onDismiss: () -> Unit,
    secondaryActionText: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
    illustration: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(Theme.radius.dialog),
            colors = CardDefaults.cardColors(
                containerColor = Theme.colorScheme.surface.background,
            ),
            modifier = Modifier.width(340.dp),
        ) {
            Column(
                modifier = Modifier.padding(Theme.spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (illustration != null) {
                    illustration()
                    Spacer(modifier = Modifier.height(Theme.spacing.lg))
                }

                Text(
                    text = title,
                    style = Theme.typography.title.large,
                    color = Theme.colorScheme.text.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(Theme.spacing.sm))

                Text(
                    text = description,
                    style = Theme.typography.body.medium,
                    color = Theme.colorScheme.text.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (content != null) {
                    content()
                }

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                AppButton(
                    text = primaryActionText,
                    onClick = onPrimaryAction,
                    innerModifier = Modifier.appShadow()
                )

                if (secondaryActionText != null && onSecondaryAction != null) {
                    Spacer(modifier = Modifier.height(Theme.spacing.sm))
                    AppButton(
                        text = secondaryActionText,
                        onClick = onSecondaryAction,
                        variant = AppButtonVariant.Text,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Confirmation Dialog")
@Composable
private fun ConfirmationDialogPreview() {
    MongezTheme {
        AppConfirmationDialog(
            title = "Delete Course?",
            description = "This action cannot be undone. All data related to this course will be permanently removed.",
            primaryActionText = "Delete",
            onPrimaryAction = {},
            onDismiss = {},
            secondaryActionText = "Cancel",
            onSecondaryAction = {},
        )
    }
}
