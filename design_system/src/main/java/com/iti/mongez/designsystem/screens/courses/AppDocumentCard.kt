package com.iti.mongez.designsystem.screens.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A reusable document/material card component matching the Figma specifications.
 * Supports file extension badges (e.g. PDF, PPT, DOC), page count, size metadata, and context action callbacks.
 */
@Composable
fun AppDocumentCard(
    title: String,
    fileSize: String,
    modifier: Modifier = Modifier,
    pageCount: Int? = null,
    fileExtension: String = "PDF",
    badgeBackgroundColor: Color = Theme.colorScheme.state.error,
    onClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(16.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(78.dp)
            .clip(shape)
            .background(Theme.colorScheme.surface.surfaceVariant)
            .border(
                width = 1.dp,
                color = Theme.colorScheme.card.border,
                shape = shape
            )
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(
                start = Theme.spacing.lg,
                top = Theme.spacing.lg,
                bottom = Theme.spacing.lg,
                end = Theme.spacing.md
            )
    ) {
        // 1. File Type Extension Badge (40dp x 40dp)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(badgeBackgroundColor)
        ) {
            Text(
                text = fileExtension.uppercase(),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.55.sp
            )
        }

        // 2. Metadata Column (Title + Subtitle)
        Column(
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.xxs),
            modifier = Modifier
                .weight(1f)
                .padding(start = Theme.spacing.lg)
        ) {
            Text(
                text = title,
                style = Theme.typography.body.medium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                ),
                color = Theme.colorScheme.text.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            val subtitleText = buildString {
                if (pageCount != null) {
                    append("$pageCount Pages  •  ")
                }
                append(fileSize)
            }

            Text(
                text = subtitleText,
                style = Theme.typography.body.small.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp
                ),
                color = Theme.colorScheme.text.tertiary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // 3. More Actions Button (32dp x 32dp)
        if (onDeleteClick != null) {
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete document",
                    tint = Theme.colorScheme.text.primary
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Document Card - Light Mode")
@Composable
private fun AppDocumentCardLightPreview() {
    MongezTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(Theme.spacing.lg)
        ) {
            AppDocumentCard(
                title = "Chapter 1 - Introduction.pdf",
                pageCount = 28,
                fileSize = "2.4 MB",
                onClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Document Card - Dark Mode")
@Composable
private fun AppDocumentCardDarkPreview() {
    MongezTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(Theme.spacing.lg)
        ) {
            AppDocumentCard(
                title = "Chapter 2 - Processes.pdf",
                pageCount = 45,
                fileSize = "3.1 MB",
                onClick = {},
                onDeleteClick = {}
            )
        }
    }
}