package com.example.tutorialrun2.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val IndigoPrimaryLight = Color(0xFF4F46E5)
val IndigoPrimaryDark = Color(0xFF818CF8)

val SlateBgLight = Color(0xFFF8FAFC)
val SlateBgDark = Color(0xFF0F172A)

val SlateSurfaceLight = Color(0xFFFFFFFF)
val SlateSurfaceDark = Color(0xFF1E293B)

// Theme.kt
private val DarkColorScheme = darkColorScheme(
    primary = IndigoPrimaryDark,
    onPrimary = Color(0xFF0F172A), // Dark text on light blue button
    background = SlateBgDark,
    onBackground = Color(0xFFF8FAFC),
    surface = SlateSurfaceDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155), // For unselected buttons
    onSurfaceVariant = Color(0xFF94A3B8)
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoPrimaryLight,
    onPrimary = Color.White,
    background = SlateBgLight,
    onBackground = Color(0xFF0F172A),
    surface = SlateSurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0), // For unselected buttons
    onSurfaceVariant = Color(0xFF64748B)
)

@Composable
fun TutorialRun2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}