package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun RoadmapEventCard(
    title: String,
    type: String = "",
    dateTime: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Theme.radius.lg),
        colors = CardDefaults.cardColors(containerColor = Theme.colorScheme.surface.surfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, Theme.colorScheme.border.secondary)
    ) {
        Column(
            modifier = Modifier.padding(Theme.spacing.md)
        ) {
            Text(
                text = title,
                style = Theme.typography.label.medium,
                color = Theme.colorScheme.text.primary,
            )
            Spacer(modifier = Modifier.height(Theme.spacing.xs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (type.isNotEmpty()) {
                    Text(
                        text = type,
                        style = Theme.typography.label.medium,
                        color = Theme.colorScheme.text.tertiary
                    )
                }
                Text(
                    text = dateTime,
                    style = Theme.typography.label.medium,
                    color = Theme.colorScheme.text.tertiary
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
private fun RoadmapEventCardPreview() {
    MongezTheme {
        RoadmapEventCard(
            title = "Algorithms Exam",
            type = "Exam",
            dateTime = "May 8 - 3:00 PM"
        )
    }
}
