package com.iti.mongez.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.components.card.AppCard
import com.iti.mongez.designsystem.theme.Theme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color
) {
    AppCard(
        modifier = modifier,
        containerColor = Color.Transparent,
        borderWidth = 1.dp,
        elevation = 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = Theme.typography.label.small,
                color = Theme.colorScheme.text.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.height(Theme.spacing.xxl)
            )
            Spacer(modifier = Modifier.height(Theme.spacing.xs))
            Text(
                text = value,
                style = Theme.typography.title.large,
                color = valueColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatCardPreview() {
    MongezTheme {
        Row(
            modifier = Modifier
                .padding(Theme.spacing.md)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Total Orders",
                value = "25",
                valueColor = Theme.colorScheme.brand.primary
            )
            Spacer(modifier = Modifier.width(Theme.spacing.md))
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Points",
                value = "1,250",
                valueColor = Theme.colorScheme.state.success
            )
        }
    }
}