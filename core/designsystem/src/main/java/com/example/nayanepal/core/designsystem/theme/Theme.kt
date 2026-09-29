package com.example.nayanepal.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NayaNepalColors.primary,
    secondary = NayaNepalColors.secondary,
    background = NayaNepalColors.darkBackground,
    surface = NayaNepalColors.darkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    error = NayaNepalColors.error
)

private val LightColorScheme = lightColorScheme(
    primary = NayaNepalColors.primary,
    secondary = NayaNepalColors.secondary,
    background = NayaNepalColors.background,
    surface = NayaNepalColors.surface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    error = NayaNepalColors.error
)

@Composable
fun NayaNepalTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkMode) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
