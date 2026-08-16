package com.iti.mongez.designsystem.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * Raw color palette — internal to the design system.
 * Feature modules must NEVER access these directly.
 * Use [com.iti.mongez.designsystem.theme.Theme.colorScheme] instead.
 */
internal object PrimitiveColors {
    val Purple200 = Color(0xFF5A67D8)
    val Purple400 = Color(0xFFA691E3)
    val Purple350 = Color(0xFFB6ACCD)
    val Purple500 = Color(0xFFA855F7)
    val Purple50 = Color(0xFFC15AF3)
    
    val Black100 = Color(0xFF0F172A)
    val Gray420 = Color(0xFFAFB0BD)
    
    val White100 = Color(0xFFF9F9FF)
    val BackgroundDark = Color(0xFF04001B)
    
    val Gray50 = Color(0xFFF3F4F6)
    val Gray100 = Color(0xFFE5E7EB)
    val Gray350 = Color(0xFFB4BACD)
    
    val Gray200 = Color(0xFFBCBCBE)
    val Gray300 = Color(0xFFB0B0B3)
    
    val Blue500 = Color(0xFF3B82F6)
    val Blue200 = Color(0xFF959FF7)
    
    val Green600 = Color(0xFF10B981)
    
    val Red600 = Color(0xFFEF4444)
    
    val Yellow50 = Color(0xFFF1CB84)
    val Orange100 = Color(0xFFF6B576)
}
