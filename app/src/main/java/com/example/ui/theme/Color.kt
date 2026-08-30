package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val MidnightBlue = Color(0xFF0B1A2F)
val MidnightDark = Color(0xFF06101D)
val IncaGold = Color(0xFFC9A84C)
val IncaGoldDark = Color(0xFFB08D32)
val IncaGoldLight = Color(0xFFE5C875)
val BackgroundLight = Color(0xFFF4F6FA)
val TextPrimary = Color(0xFF1C1C1E)
val TextSecondary = Color(0xFF6B7280)
val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceBorder = Color(0xFFF1F3F9)
val SurfaceVariant = Color(0xFFEBEFF5)
val SuccessGreen = Color(0xFF2E7D32)
val ErrorRed = Color(0xFFD32F2F)
val DarkBackground = Color(0xFF061120)
val DarkSurface = Color(0xFF112239)

val MidnightGradient = Brush.verticalGradient(
    colors = listOf(MidnightBlue, MidnightDark)
)

val GoldGradient = Brush.linearGradient(
    colors = listOf(IncaGold, IncaGoldDark)
)

val ScrimGradient = Brush.verticalGradient(
    colors = listOf(Color.Transparent, MidnightBlue.copy(alpha = 0.9f))
)


