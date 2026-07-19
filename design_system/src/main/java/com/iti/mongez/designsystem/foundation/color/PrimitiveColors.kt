package com.iti.mongez.designsystem.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * Raw color palette — internal to the design system.
 * Feature modules must NEVER access these directly.
 * Use [com.iti.mongez.designsystem.theme.Theme.colorScheme] instead.
 */
internal object PrimitiveColors {
    // region Purple
    val Purple50 = Color(0xFFF5F3FF)
    val Purple100 = Color(0xFFEDE9FE)
    val Purple200 = Color(0xFFDDD6FE)
    val Purple300 = Color(0xFFC4B5FD)
    val Purple400 = Color(0xFFA78BFA)
    val Purple500 = Color(0xFF8B5CF6)
    val Purple600 = Color(0xFF5A67D8)
    val Purple700 = Color(0xFF4C3FE0)
    val Purple800 = Color(0xFF4338CA)
    val Purple900 = Color(0xFF312E81)
    // endregion

    // region Gray
    val Gray50 = Color(0xFFFCFCFD)
    val Gray75 = Color(0xFFF6F7FB)
    val Gray100 = Color(0xFFF8F9FC)
    val Gray200 = Color(0xFFF2F4F7)
    val Gray250 = Color(0xFFE5E7EB)
    val Gray300 = Color(0xFFEAECF0)
    val Gray400 = Color(0xFFD0D5DD)
    val Gray500 = Color(0xFF98A2B3)
    val Gray600 = Color(0xFF667085)
    val Gray700 = Color(0xFF475467)
    val Gray800 = Color(0xFF344054)
    val Gray900 = Color(0xFF101828)
    // endregion

    // region Green
    val Green50 = Color(0xFFECFDF5)
    val Green100 = Color(0xFFD1FAE5)
    val Green200 = Color(0xFFA7F3D0)
    val Green300 = Color(0xFF6EE7B7)
    val Green400 = Color(0xFF34D399)
    val Green500 = Color(0xFF10B981)
    val Green600 = Color(0xFF059669)
    val Green700 = Color(0xFF047857)
    // endregion

    // region Red
    val Red50 = Color(0xFFFEF2F2)
    val Red100 = Color(0xFFFEE2E2)
    val Red200 = Color(0xFFFECACA)
    val Red300 = Color(0xFFFCA5A5)
    val Red400 = Color(0xFFF87171)
    val Red500 = Color(0xFFEF4444)
    val Red600 = Color(0xFFDC2626)
    val Red700 = Color(0xFFB91C1C)
    // endregion

    // region Orange
    val Orange50 = Color(0xFFFFF7ED)
    val Orange100 = Color(0xFFFFEDD5)
    val Orange200 = Color(0xFFFED7AA)
    val Orange300 = Color(0xFFFDBA74)
    val Orange400 = Color(0xFFFB923C)
    val Orange500 = Color(0xFFF97316)
    val Orange600 = Color(0xFFEA580C)
    // endregion

    // region Blue
    val Blue50 = Color(0xFFEFF6FF)
    val Blue100 = Color(0xFFDBEAFE)
    val Blue200 = Color(0xFFBFDBFE)
    val Blue300 = Color(0xFF93C5FD)
    val Blue400 = Color(0xFF60A5FA)
    val Blue500 = Color(0xFF3B82F6)
    val Blue600 = Color(0xFF2563EB)
    // endregion

    // region Neutrals
    val White = Color(0xFFF9F9FF)
    val Black = Color(0xFF000000)
    // endregion
}
