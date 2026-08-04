package com.iti.mongez.designsystem.components.textfield

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Standard text field for the Mongez application.
 *
 * @param value Current text value.
 * @param onValueChange Callback for text changes.
 * @param modifier Modifier for external layout.
 * @param label Optional label above the field.
 * @param placeholder Hint text shown when empty.
 * @param leadingIcon Optional icon at the start.
 * @param trailingIcon Optional icon at the end.
 * @param isError Whether the field displays an error state.
 * @param errorMessage Error text displayed below the field.
 * @param helperText Helper text displayed below the field.
 * @param enabled Whether the field is interactive.
 * @param readOnly Whether the field is read-only.
 * @param singleLine Whether to limit to a single line.
 * @param keyboardOptions Keyboard configuration.
 * @param keyboardActions IME action handlers.
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    helperText: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = Theme.typography.label.medium,
                color = Theme.colorScheme.text.primary,
                modifier = Modifier.padding(bottom = Theme.spacing.xs),
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = Theme.typography.body.medium,
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                        style = Theme.typography.body.medium,
                        color = Theme.colorScheme.input.placeholder,
                    )
                }
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Theme.colorScheme.input.icon,
                    )
                }
            },
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(Theme.radius.lg),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Theme.colorScheme.input.text,
                unfocusedTextColor = Theme.colorScheme.input.text,
                disabledTextColor = Theme.colorScheme.text.disabled,
                focusedBorderColor = Theme.colorScheme.input.focusedBorder.copy(alpha = 0.5f),
                unfocusedBorderColor = Theme.colorScheme.input.border,
                errorBorderColor = Theme.colorScheme.input.errorBorder,
                disabledBorderColor = Theme.colorScheme.border.disabled,
                focusedContainerColor = Theme.colorScheme.surface.background,
                unfocusedContainerColor = Theme.colorScheme.surface.background,
                errorContainerColor = Theme.colorScheme.surface.background,
                cursorColor = Theme.colorScheme.brand.primary,
                errorCursorColor = Theme.colorScheme.state.error,
                focusedLeadingIconColor = Theme.colorScheme.brand.primary,
                unfocusedLeadingIconColor = Theme.colorScheme.input.icon,
            ),
        )

        // Error or helper text
        val supportText = if (isError && errorMessage != null) errorMessage else helperText
        if (supportText != null) {
            Spacer(modifier = Modifier.height(Theme.spacing.xs))
            Text(
                text = supportText,
                style = Theme.typography.body.small,
                color = if (isError) Theme.colorScheme.state.error
                else Theme.colorScheme.text.tertiary,
                modifier = Modifier.padding(start = Theme.spacing.xs),
            )
        }
    }
}

/**
 * Password text field with built-in visibility toggle.
 */
@Composable
fun AppPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    AppTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Rounded.VisibilityOff
                    else Icons.Rounded.Visibility,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = Theme.colorScheme.input.icon,
                )
            }
        },
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Default TextField")
@Composable
private fun DefaultTextFieldPreview() {
    MongezTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            placeholder = "youremail@example.com",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Password TextField")
@Composable
private fun PasswordTextFieldPreview() {
    MongezTheme {
        AppPasswordTextField(
            value = "password123",
            onValueChange = {},
            label = "Password",
            placeholder = "Enter password",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Error TextField")
@Composable
private fun ErrorTextFieldPreview() {
    MongezTheme {
        AppTextField(
            value = "invalid",
            onValueChange = {},
            label = "Email",
            isError = true,
            errorMessage = "Please enter a valid email",
            modifier = Modifier.padding(16.dp),
        )
    }
}
