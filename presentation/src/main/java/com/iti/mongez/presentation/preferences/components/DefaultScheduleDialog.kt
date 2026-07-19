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
    onDismiss: () -> Unit,
    onGoToSettings: () -> Unit
) {
    AppConfirmationDialog(
        title = stringResource(id = R.string.schedule_default_title),
        description = stringResource(id = R.string.schedule_default_description),
        primaryActionText = stringResource(id = R.string.got_it),
        onPrimaryAction = onDismiss,
        onDismiss = onDismiss,
        secondaryActionText = stringResource(id = R.string.go_to_settings),
        onSecondaryAction = onGoToSettings,
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
                            Color(0xFFF6F7FB),
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
                                text = stringResource(id = R.string.default_study_hours_value),
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
                                listOf("Sun", "Mon", "Tue", "Wed", "Thu").forEach { day ->
                                    AppChip(
                                        label = day,
                                        selected = true,
                                        onSelectedChange = {},
                                        enabled = true,
                                        selectedContainerColor = Color(0xFFEEEBFF),
                                        selectedLabelColor = Color(0xFF5B4FE9)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                val hintText = stringResource(id = R.string.change_settings_hint)
                val settingsWord = stringResource(id = R.string.go_to_settings)
                val annotatedHint = buildAnnotatedString {
                    val parts = hintText.split(settingsWord)
                    if (parts.size > 1) {
                        append(parts[0])
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF5B4FE9),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(settingsWord)
                        }
                        append(parts[1])
                    } else {
                        append(hintText)
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
                onDismiss = {},
                onGoToSettings = {}
            )
        }
    }
}
