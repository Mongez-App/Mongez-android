package com.iti.mongez.designsystem.components.sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Modal bottom sheet wrapper with consistent styling.
 *
 * @param onDismiss Callback when the sheet is dismissed.
 * @param modifier Modifier.
 * @param title Optional title text shown at the top.
 * @param sheetState State controlling the sheet.
 * @param content Composable content inside the sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = Theme.radius.sheet,
            topEnd = Theme.radius.sheet,
        ),
        containerColor = Theme.colorScheme.surface.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = Theme.spacing.sm)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(Theme.radius.full)),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.xl),
        ) {
            if (title != null) {
                Text(
                    text = title,
                    style = Theme.typography.title.large,
                    color = Theme.colorScheme.text.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Theme.spacing.lg),
                )
            }

            content()

            Spacer(modifier = Modifier.height(Theme.spacing.xl))
        }
    }
}
