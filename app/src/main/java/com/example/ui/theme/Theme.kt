package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = KptcNavy,
    onPrimary = Color.White,
    primaryContainer = SoftIceContainer,
    onPrimaryContainer = KptcNavy,
    secondary = KptcBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCEBF8),
    onSecondaryContainer = KptcNavy,
    tertiary = CoastalTeal,
    onTertiary = Color.White,
    tertiaryContainer = CoastalTealContainer,
    onTertiaryContainer = Color(0xFF064852),
    background = PaleCanvas,
    onBackground = InkDark,
    surface = SurfaceWhite,
    onSurface = InkDark,
    surfaceVariant = Color(0xFFE7EEF6),
    onSurfaceVariant = MutedSlate,
    outline = SoftBorder,
    outlineVariant = Color(0xFFE6EDF5)
)

private val DarkColorScheme = darkColorScheme(
    primary = KptcSkyAccent,
    onPrimary = KptcNavyDeep,
    primaryContainer = DarkNoticeBg,
    onPrimaryContainer = Color(0xFFD8EBFF),
    secondary = Color(0xFF79BCEF),
    onSecondary = KptcNavyDeep,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = Color(0xFFD6E8FA),
    tertiary = Color(0xFF4CC9DA),
    onTertiary = Color(0xFF00363D),
    tertiaryContainer = Color(0xFF0B4650),
    onTertiaryContainer = Color(0xFFBDF2FA),
    background = DarkCanvas,
    onBackground = DarkInk,
    surface = DarkSurface,
    onSurface = DarkInk,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkMuted,
    outline = DarkOutline,
    outlineVariant = Color(0xFF1F364E)
)

@Composable
fun KptcKuwaitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KptcTypography,
        content = content
    )
}
