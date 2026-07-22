package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme
import java.time.LocalDate

@Composable
fun CalendarDay(
    date: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isStart = date == startDate
    val isEnd = date == endDate
    val inRange = startDate != null && endDate != null && date.isAfter(startDate) && date.isBefore(endDate)

    val backgroundColor = when {
        isStart || isEnd -> Theme.colorScheme.brand.primary
        inRange -> Theme.colorScheme.brand.primaryContainer
        else -> Color.Transparent
    }

    val textColor = when {
        isStart || isEnd -> Theme.colorScheme.brand.onPrimary
        inRange -> Theme.colorScheme.brand.primary
        else -> Theme.colorScheme.text.primary
    }

    val shape = when {
        isStart && isEnd -> CircleShape
        isStart -> RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50)
        isEnd -> RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50)
        inRange -> RoundedCornerShape(0)
        else -> CircleShape
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .padding(vertical = 4.dp)
            .clip(shape)
            .background(backgroundColor)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            style = Theme.typography.body.medium
        )
    }
}