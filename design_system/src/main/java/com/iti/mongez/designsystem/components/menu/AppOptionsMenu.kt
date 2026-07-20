package com.iti.mongez.designsystem.components.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme

/**
 * Reusable dropdown menu that matches the exact Figma specs for the Course Options
 * (180px width, 16px radius, specific padding, and precise text/icon colors).
 */
@Composable
fun AppCourseOptionsMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .width(180.dp) // Figma: width: 180px
            .border(
                width = 1.dp,
                color = Color(0xFFF9FAFB), // Figma: border: 1px solid #F9FAFB
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp), // Figma: border-radius: 16px
        containerColor = Color.White, // Figma: background: #FFFFFF
        shadowElevation = 8.dp // Figma box-shadow approximation
    ) {

        // 1. Edit Course Button
        AppDropdownMenuItem(
            text = "Edit Course",
            icon = Icons.Outlined.Edit,
            iconColor = Color(0xFF6B7280),
            textColor = Color(0xFF374151),
            height = 45.dp, // Figma: height: 45px
            paddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            onClick = {
                onEditClick()
                onDismissRequest()
            }
        )

        // 2. Delete Course Button
        AppDropdownMenuItem(
            text = "Delete Course",
            icon = Icons.Outlined.Delete,
            iconColor = Color(0xFFEF4444),
            textColor = Color(0xFFEF4444),
            height = 49.dp, // Figma: height: 49px
            paddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp),
            onClick = {
                onDeleteClick()
                onDismissRequest()
            }
        )
    }
}

/**
 * Internal component to precisely match the Figma Button layout specs
 * bypassing standard Material DropdownMenuItem padding restrictions.
 */
@Composable
private fun AppDropdownMenuItem(
    text: String,
    icon: ImageVector,
    iconColor: Color,
    textColor: Color,
    height: Dp,
    paddingValues: PaddingValues,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp), // Figma: gap 12px
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clickable(onClick = onClick)
            .padding(paddingValues)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconColor,
            modifier = Modifier.size(16.dp) // Figma: width/height: ~15px
        )

        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp, // Figma: font-size: 14px
            fontWeight = FontWeight.Medium, // Figma: font-weight: 500
            lineHeight = 21.sp // Figma: line-height: 21px
        )
    }
}

// ──────────────────────────────────────────────────────────────
// Implementation Example & Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF3F4F6)
@Composable
private fun AppCourseOptionsMenuPreview() {
    MongezTheme {
        var expanded by remember { mutableStateOf(true) } // Set true to see in preview

        Box(modifier = Modifier.padding(64.dp)) {
            // Anchor element (The 3 dots)
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Outlined.MoreHoriz,
                    contentDescription = "More options"
                )
            }

            // The Menu
            AppCourseOptionsMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onEditClick = { /* Handle Edit */ },
                onDeleteClick = { /* Handle Delete */ }
            )
        }
    }
}