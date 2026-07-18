package com.iti.mongez.designsystem.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun AppDeadlineCard(
    subject: String,
    taskType: String,
    timeLeft: String,
    tintColor: Color,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .size(163.dp, 90.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colorScheme.surface.background)
            .border(
                1.dp,
                tintColor.copy(alpha = .5f),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = subject,
                color = tintColor,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 12.sp,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = taskType,
                color = Theme.colorScheme.text.primary,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 16.sp
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = timeLeft,
                color = tintColor,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(tintColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
/**
 * Preview matching the provided screenshot exactly, drawing the background
 * natively from the theme to prevent false-color mismatch issues.
 */
@Preview(showBackground = true, name = "Deadline Cards Row")
@Composable
private fun AppDeadlineCardsPreview() {
    MongezTheme {
        Row(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Red Card -> Using state.error
            AppDeadlineCard(
                subject = "Networks",
                taskType = "Assignment",
                timeLeft = "Tomorrow",
                tintColor = Theme.colorScheme.state.error
            )

            // Green Card -> Using state.success
            AppDeadlineCard(
                subject = "Operating Systems",
                taskType = "Midterm",
                timeLeft = "4 days left",
                tintColor = Theme.colorScheme.state.success
            )
        }
    }
}