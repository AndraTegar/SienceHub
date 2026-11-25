package com.kelompoksix.siencehub.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.kelompoksix.siencehub.utils.AppSettingsManager

private fun buildDarkColorScheme() = darkColorScheme(
    primary = Color(0xFF819A7C),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF29382B),
    onPrimaryContainer = Color(0xFFD9E8D5),
    secondary = GreenLight,
    onSecondary = Color(0xFF121714),
    tertiary = Cream90,
    background = Color(0xFF121714),
    surface = Color(0xFF1D2420),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF252E28),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF2E3A32)
)

private fun buildLightColorScheme() = lightColorScheme(
    primary = Color(0xFF7A8B76),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE3EAE1),
    onPrimaryContainer = Color(0xFF5C6D58),
    secondary = GreenLight,
    onSecondary = Color(0xFF1E293B),
    tertiary = GrayColor,
    background = Color(0xFFF7F9F8),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1E293B),
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFE2E8F0)
)

@Composable
fun SienceHubTheme(
    darkTheme: Boolean = AppSettingsManager.isDarkMode,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> buildDarkColorScheme()
        else -> buildLightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
