// Theme.kt
package com.damiens.mjoba.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SafaricomLightColorScheme = lightColorScheme(
    primary = SafaricomGreen,
    onPrimary = Color.White,
    primaryContainer = SafaricomLightGreen,
    onPrimaryContainer = Color(0xFF002110),

    secondary = SafaricomDarkGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFAEF1C1),
    onSecondaryContainer = Color(0xFF002110),

    tertiary = Color(0xFF006D3B),
    onTertiary = Color.White,

    background = SafaricomBackground,
    onBackground = Color(0xFF1A1C19),

    surface = SafaricomSurface,
    onSurface = Color(0xFF1A1C19),

    error = SafaricomError,
    onError = Color.White
)

private val SafaricomDarkColorScheme = darkColorScheme(
    primary = Color(0xFF80F999),
    onPrimary = Color(0xFF003915),
    primaryContainer = SafaricomGreen,
    onPrimaryContainer = Color(0xFFA7F4B4),

    secondary = Color(0xFF93D5A0),
    onSecondary = Color(0xFF00391B),
    secondaryContainer = Color(0xFF005229),
    onSecondaryContainer = Color(0xFFAEF1BB),

    tertiary = Color(0xFF8BD9B1),
    onTertiary = Color(0xFF003823),

    background = Color(0xFF191C19),
    onBackground = Color(0xFFE2E3DD),

    surface = Color(0xFF121410),
    onSurface = Color(0xFFE2E3DD),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun MJobaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SafaricomDarkColorScheme else SafaricomLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}