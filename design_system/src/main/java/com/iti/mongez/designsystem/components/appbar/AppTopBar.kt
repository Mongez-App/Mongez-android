package com.iti.mongez.designsystem.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Standard top app bar.
 *
 * @param title Title text.
 * @param modifier Modifier for external layout.
 * @param onBackClick Optional back navigation callback. If null, no back button is shown.
 * @param actions Optional trailing action icons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = Theme.typography.title.medium,
            )
        },
        modifier = modifier.height(64.dp),
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Navigate back",
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Theme.colorScheme.surface.background,
            titleContentColor = Theme.colorScheme.text.primary,
            navigationIconContentColor = Theme.colorScheme.text.primary,
            actionIconContentColor = Theme.colorScheme.text.secondary,
        ),
    )
}

@Preview(showBackground = true, name = "Top Bar with Back")
@Composable
private fun TopBarWithBackPreview() {
    MongezTheme {
        AppTopBar(title = "Add New Course", onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Top Bar without Back")
@Composable
private fun TopBarWithoutBackPreview() {
    MongezTheme {
        AppTopBar(title = "My Courses")
    }
}
