package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = IncaGold,
    onPrimary = MidnightBlue,
    primaryContainer = MidnightBlue,
    onPrimaryContainer = IncaGoldLight,
    secondary = IncaGold,
    onSecondary = MidnightBlue,
    background = DarkBackground,
    onBackground = SurfaceWhite,
    surface = DarkSurface,
    onSurface = SurfaceWhite,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = TextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = MidnightBlue,
    onPrimary = SurfaceWhite,
    primaryContainer = MidnightBlue,
    onPrimaryContainer = IncaGold,
    secondary = IncaGold,
    onSecondary = MidnightBlue,
    secondaryContainer = IncaGoldLight,
    onSecondaryContainer = MidnightBlue,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary
)

@Composable
fun RutaPeruTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

