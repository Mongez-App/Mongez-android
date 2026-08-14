package com.iti.mongez.presentation.coursedetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EditCourseSheetContent(
    initialName: String,
    initialImageUrl: String,
    courseCode: String,
    startDateIso: String,
    examDateIso: String,
    courseUrl: String,
    isOnline: Boolean,
    isLoading: Boolean,
    onCancel: () -> Unit,
    onSave: (name: String, imageUrl: String) -> Unit
) {
    var courseName by remember { mutableStateOf(initialName) }
    var imageUrl by remember { mutableStateOf(initialImageUrl) }

    fun formatIsoToUi(isoString: String): String {
        return try {
            val instant = Instant.parse(isoString)
            instant.atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            isoString
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.lg)
    ) {
        // Editable fields
        AppTextField(
            value = courseName,
            onValueChange = { courseName = it },
            label = stringResource(R.string.label_course_name),
            placeholder = stringResource(R.string.hint_course_name),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            )
        )

        AppTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = "Course Image URL",
            placeholder = "https://..."
        )

        // Read-only fields
        if (isOnline) {
            AppTextField(
                value = courseUrl,
                onValueChange = {},
                label = stringResource(R.string.label_course_url),
                readOnly = true,
                placeholder = "https://..."
            )
        }

        AppTextField(
            value = courseCode,
            onValueChange = {},
            label = stringResource(R.string.label_course_code),
            readOnly = true
        )

        AppTextField(
            value = formatIsoToUi(startDateIso),
            onValueChange = {},
            label = stringResource(R.string.label_start_date),
            readOnly = true
        )

        AppTextField(
            value = formatIsoToUi(examDateIso),
            onValueChange = {},
            label = stringResource(R.string.label_exam_date),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.action_cancel),
                    color = Theme.colorScheme.text.secondary
                )
            }

            AppButton(
                text = "Save Changes",
                onClick = { onSave(courseName, imageUrl) },
                modifier = Modifier.weight(1f),
                variant = AppButtonVariant.Primary,
                isLoading = isLoading
            )
        }
    }
}
