package com.iti.mongez.presentation.preferences.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.chip.AppChip
import com.iti.mongez.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefaultScheduleDialog(
    studyHours: Int,
    selectedDays: List<String>,
    onDismiss: () -> Unit,
    onGoToProfile: () -> Unit
) {
    AppConfirmationDialog(
        title = stringResource(id = R.string.schedule_default_title),
        description = stringResource(id = R.string.schedule_default_description),
        primaryActionText = stringResource(id = R.string.got_it),
        onPrimaryAction = onDismiss,
        onDismiss = onDismiss,
        secondaryActionText = stringResource(id = R.string.go_to_profile),
        onSecondaryAction = onGoToProfile,
        secondaryActionContainerColor = Theme.colorScheme.surface.surfaceVariant,
        illustration = {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Theme.colorScheme.brand.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    colorFilter = ColorFilter.tint(Theme.colorScheme.brand.primary)
                )
            }
        },
        content = {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Theme.spacing.lg)
                        .background(
                            Theme.colorScheme.surface.surfaceVariant,
                            RoundedCornerShape(Theme.radius.lg)
                        )
                        .padding(Theme.spacing.lg)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Theme.colorScheme.surface.surfaceVariant,
                                    RoundedCornerShape(Theme.spacing.md)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.clock),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified,
                                )
                        }
                        Spacer(modifier = Modifier.width(Theme.spacing.md))
                        Column {
                            Text(
                                text = stringResource(id = R.string.study_time_label),
                                style = Theme.typography.label.medium,
                                color = Theme.colorScheme.text.tertiary
                            )
                            Text(
                                text = stringResource(id = R.string.hours_per_day, studyHours),
                                style = Theme.typography.title.small,
                                fontWeight = FontWeight.Bold,
                                color = Theme.colorScheme.text.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Theme.spacing.lg))

                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Theme.colorScheme.surface.surfaceVariant,
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.days),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(Theme.spacing.md))
                        Column {
                            Text(
                                text = stringResource(id = R.string.study_days_label),
                                style = Theme.typography.label.medium,
                                color = Theme.colorScheme.text.tertiary
                            )
                            Spacer(modifier = Modifier.height(Theme.spacing.xs))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(Theme.spacing.xs),
                                verticalArrangement = Arrangement.spacedBy(Theme.spacing.xs)
                            ) {
                                selectedDays.forEach { day ->
                                    AppChip(
                                        label = day,
                                        selected = true,
                                        onSelectedChange = {},
                                        enabled = true,
                                        selectedContainerColor = Theme.colorScheme.brand.primaryContainer,
                                        selectedLabelColor = Theme.colorScheme.brand.primary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                val hintText = stringResource(id = R.string.change_settings_hint)
                val targetWord = stringResource(id = R.string.go_to_profile)
                val annotatedHint = buildAnnotatedString {
                    append(hintText)
                    val start = hintText.indexOf(targetWord)
                    if (start != -1) {
                        addStyle(
                            style = SpanStyle(
                                color = Theme.colorScheme.brand.primary,
                                fontWeight = FontWeight.Bold
                            ),
                            start = start,
                            end = start + targetWord.length
                        )
                    }
                }

                Text(
                    text = annotatedHint,
                    style = Theme.typography.body.small,
                    color = Theme.colorScheme.text.tertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800, apiLevel = 34)
@Composable
fun DefaultScheduleDialogPreview() {
    MongezTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            DefaultScheduleDialog(
                studyHours = 6,
                selectedDays = listOf("Sat", "Sun"),
                onDismiss = {},
                onGoToProfile = {}
            )
        }
    }
}
