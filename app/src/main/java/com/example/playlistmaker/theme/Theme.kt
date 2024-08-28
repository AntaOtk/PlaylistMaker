package com.example.playlistmaker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = BackgroundColor,
    secondary = YaBlack,
    onTertiary = BlueText,
    tertiary = TextGray,
    background = White,
    surface = LightGray,
    onPrimary = YaBlack,
    onSecondary = ElementsColor,
    onBackground = YaBlack,
    onSurface = TextGray,
    surfaceVariant = LightGray,
    onSurfaceVariant = TextGray,
    surfaceTint = White
)

private val DarkColorPalette = darkColorScheme(
    primary = BackgroundNavbar,
    secondary = White,
    onTertiary = BlueText,
    tertiary = Color(0xfff6d54f),
    background = YaBlack,
    surface = White,
    onPrimary = White,
    onSecondary = LightGray,
    onBackground = White,
    onSurface = White,
    surfaceVariant = White,
    onSurfaceVariant = YaBlack,
    surfaceTint = YaBlack
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        content = content
    )
}