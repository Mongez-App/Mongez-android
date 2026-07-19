package com.iti.mongez.designsystem.components.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A reusable card component to display uploaded files (materials).
 * Strictly uses Mongez AppColorScheme to adapt to Light and Dark modes.
 * Includes a trailing close button to remove the file.
 */
@Composable
fun AppUploadedFileCard(
    fileName: String,
    fileSize: String,
    fileExtension: String,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Map component colors using the Theme
    val cardBackgroundColor = Theme.colorScheme.surface.surfaceVariant
    val cardBorderColor = Theme.colorScheme.border.secondary

    val titleColor = Theme.colorScheme.text.primary
    val sizeColor = Theme.colorScheme.text.tertiary
    val closeIconColor = Theme.colorScheme.text.secondary

    // Determine badge color based on file extension (Defaults to standard surface, overrides for PDF)
    val isPdf = fileExtension.equals("pdf", ignoreCase = true)
    val badgeBackgroundColor = if (isPdf) Theme.colorScheme.state.errorContainer else Theme.colorScheme.surface.surfaceHigh
    val badgeTextColor = if (isPdf) Theme.colorScheme.state.error else Theme.colorScheme.text.secondary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(74.dp) // Fixed height from CSS
            .background(cardBackgroundColor, RoundedCornerShape(16.dp))
            .border(1.dp, cardBorderColor, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 2. Dynamic File Extension Badge
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .background(badgeBackgroundColor, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = fileExtension.uppercase(),
                color = badgeTextColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 3. Truncated File Name
        Text(
            text = fileName,
            color = titleColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f) // Takes up remaining space, pushing the size text to the end
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 4. File Size (Constrained width to force wrapping as seen in Figma)
        Text(
            text = fileSize,
            color = sizeColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(18.dp) // Narrow width forces "2.4" and "MB" to stack
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 5. Remove Button
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Remove uploaded file",
            tint = closeIconColor,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .clickable { onRemoveClick() }
        )
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "File Card - Light Theme (PDF)")
@Composable
private fun AppUploadedFileCardLightPreview() {
    MongezTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(24.dp)
        ) {
            AppUploadedFileCard(
                fileName = "Chapter 1 - Introduction.pdf",
                fileSize = "2.4 MB",
                fileExtension = "pdf",
                onRemoveClick = { /* Handle remove logic */ },
                modifier = Modifier.width(300.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "File Card - Dark Theme (Doc)")
@Composable
private fun AppUploadedFileCardDarkPreview() {
    MongezTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(24.dp)
        ) {
            AppUploadedFileCard(
                fileName = "Course Syllabus.docx",
                fileSize = "500 KB",
                fileExtension = "doc",
                onRemoveClick = { /* Handle remove logic */ },
                modifier = Modifier.width(300.dp)
            )
        }
    }
}