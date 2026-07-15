package com.iti.mongez.designsystem.components.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Section header with title and optional "View all" action.
 *
 * @param title Section title.
 * @param modifier Modifier.
 * @param actionText Optional action text (e.g. "View all").
 * @param onAction Callback for the action text click.
 */
@Composable
fun AppSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = Theme.typography.title.medium,
            color = Theme.colorScheme.text.primary,
        )

        if (actionText != null && onAction != null) {
            Text(
                text = actionText,
                style = Theme.typography.label.medium,
                color = Theme.colorScheme.brand.primary,
                modifier = Modifier.clickable(onClick = onAction),
            )
        }
    }
}

@Preview(showBackground = true, name = "Section Header")
@Composable
private fun SectionHeaderPreview() {
    MongezTheme {
        AppSectionHeader(
            title = "Today's Tasks",
            actionText = "View all",
            onAction = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Section Header - No Action")
@Composable
private fun SectionHeaderNoActionPreview() {
    MongezTheme {
        AppSectionHeader(
            title = "Upcoming Deadlines",
            modifier = Modifier.padding(16.dp),
        )
    }
}
