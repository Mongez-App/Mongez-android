package com.iti.mongez.designsystem.components.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Top search bar shared across Home and Courses screens.
 *
 * @param query Current search text.
 * @param onQueryChange Callback for text changes.
 * @param modifier Modifier for external layout.
 * @param placeholder Hint text.
 * @param onClear Callback to clear the search.
 * @param onFilterClick Optional callback for filter action.
 */
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search courses...",
    onClear: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        textStyle = Theme.typography.body.medium,
        placeholder = {
            Text(
                text = placeholder,
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.input.placeholder,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = Theme.colorScheme.input.icon,
            )
        },
        trailingIcon = {
            if (query.isNotEmpty() && onClear != null) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear search",
                        tint = Theme.colorScheme.input.icon,
                    )
                }
            } else if (onFilterClick != null) {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Rounded.Tune,
                        contentDescription = "Filter",
                        tint = Theme.colorScheme.input.icon,
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(Theme.radius.lg),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Theme.colorScheme.input.text,
            unfocusedTextColor = Theme.colorScheme.input.text,
            focusedBorderColor = Theme.colorScheme.input.focusedBorder.copy(alpha = 0.9f),
            unfocusedBorderColor = Theme.colorScheme.text.secondary,
            focusedContainerColor = Theme.colorScheme.surface.background,
            unfocusedContainerColor = Theme.colorScheme.surface.background,
            cursorColor = Theme.colorScheme.brand.primary,
        ),
    )
}

@Preview(showBackground = true, name = "Search Bar - Empty")
@Composable
private fun SearchBarEmptyPreview() {
    MongezTheme {
        AppSearchBar(
            query = "",
            onQueryChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Search Bar - With Query")
@Composable
private fun SearchBarFilledPreview() {
    MongezTheme {
        AppSearchBar(
            query = "Operating Systems",
            onQueryChange = {},
            onClear = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
