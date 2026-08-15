package com.iti.mongez.presentation.coursedetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.loading.AppShimmer
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun CoursesShimmerLoading() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg)
    ) {
        repeat(3) {
            ShimmerCourseCard()
        }
    }
}

@Composable
fun ShimmerCourseCard() {
    val radius = Theme.radius
    val spacing = Theme.spacing

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Theme.colorScheme.card.background,
                shape = RoundedCornerShape(radius.dialog)
            )
            .padding(spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppShimmer(
            modifier = Modifier
                .size(width = 128.dp, height = 144.dp)
                .clip(RoundedCornerShape(radius.lg))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .height(144.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                AppShimmer(height = 24.dp, modifier = Modifier.fillMaxWidth(0.8f))
                AppShimmer(height = 12.dp, modifier = Modifier.fillMaxWidth(0.4f))
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppShimmer(height = 20.dp, modifier = Modifier.width(40.dp))
                    AppShimmer(height = 10.dp, modifier = Modifier.width(30.dp))
                }

                AppShimmer(
                    height = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                )
            }
        }
    }
}


