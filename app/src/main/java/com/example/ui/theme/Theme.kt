package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueContainerLight,
    onPrimaryContainer = BlueDark,

    secondary = BlueLight,
    onSecondary = Color.White,
    secondaryContainer = BlueContainerLight,
    onSecondaryContainer = BlueDark,

    tertiary = AmberTrust,
    onTertiary = Color.White,
    tertiaryContainer = AmberTrustContainer,
    onTertiaryContainer = Color(0xFF78350F),

    background = CanvasLight,
    onBackground = TextPrimaryLight,

    surface = SurfaceLight,
    onSurface = TextPrimaryLight,

    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,

    surfaceTint = BluePrimary,
    outline = BorderLight,
    outlineVariant = SkyBorderLight,

    error = CrimsonAlert,
    onError = Color.White,
    errorContainer = CrimsonAlertContainer,
    onErrorContainer = Color(0xFF7F1D1D)
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    onPrimary = Color(0xFF002B49),
    primaryContainer = BlueContainerDark,
    onPrimaryContainer = Color(0xFFBAE0FD),

    secondary = BluePrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF0C3854),
    onSecondaryContainer = Color(0xFF7DD3FC),

    tertiary = AmberTrustLight,
    onTertiary = Color(0xFF452B00),
    tertiaryContainer = Color(0xFF5A3600),
    onTertiaryContainer = AmberTrustContainer,

    background = CanvasDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,

    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,

    surfaceTint = BlueLight,
    outline = BorderDark,
    outlineVariant = Color(0xFF16324A),

    error = CrimsonAlert,
    onError = Color.White,
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFCA5A5)
)

@Composable
fun NirapodTheme(
    darkTheme: Boolean = false, // LIGHT THEME IS THE DEFAULT AS SPECIFIED
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NirapodTypography,
        shapes = NirapodShapes,
        content = content
    )
}

// Support preview theme alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    NirapodTheme(darkTheme = darkTheme, content = content)
}
