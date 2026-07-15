package com.iti.mongez.designsystem.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Consistent empty state component shown instead of blank screens.
 *
 * @param title Main message (e.g. "No Courses Yet").
 * @param description Supporting text.
 * @param modifier Modifier.
 * @param illustration Optional composable slot for an illustration or icon.
 * @param actionText Optional CTA button text.
 * @param onAction Callback for the CTA button.
 */
@Composable
fun AppEmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    illustration: @Composable (() -> Unit)? = null,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = Theme.spacing.xxl),
        ) {
            if (illustration != null) {
                illustration()
                Spacer(modifier = Modifier.height(Theme.spacing.xl))
            }

            Text(
                text = title,
                style = Theme.typography.title.large,
                color = Theme.colorScheme.text.primary,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(Theme.spacing.sm))

            Text(
                text = description,
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.text.secondary,
                textAlign = TextAlign.Center,
            )

            if (actionText != null && onAction != null) {
                Spacer(modifier = Modifier.height(Theme.spacing.xl))
                AppButton(
                    text = actionText,
                    onClick = onAction,
                    fullWidth = false,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun EmptyStatePreview() {
    MongezTheme {
        AppEmptyState(
            title = "No Courses Yet",
            description = "Start by adding your first course to begin your learning journey.",
            illustration = {
                Icon(
                    imageVector = Icons.Rounded.Inbox,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Theme.colorScheme.text.hint,
                )
            },
            actionText = "Add Course",
            onAction = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty State - No Action")
@Composable
private fun EmptyStateNoActionPreview() {
    MongezTheme {
        AppEmptyState(
            title = "No Search Results",
            description = "Try searching with different keywords.",
            illustration = {
                Icon(
                    imageVector = Icons.Rounded.Inbox,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Theme.colorScheme.text.hint,
                )
            },
        )
    }
}
