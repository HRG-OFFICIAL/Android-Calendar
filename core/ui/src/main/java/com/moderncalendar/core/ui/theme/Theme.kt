package com.moderncalendar.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = CalendarPrimary,
    onPrimary = CalendarOnPrimary,
    primaryContainer = CalendarDarkPrimaryContainer,
    onPrimaryContainer = CalendarDarkOnPrimaryContainer,
    secondary = CalendarSecondary,
    onSecondary = CalendarOnSecondary,
    secondaryContainer = CalendarDarkSecondaryContainer,
    onSecondaryContainer = CalendarDarkOnSecondaryContainer,
    tertiary = CalendarTertiary,
    onTertiary = CalendarOnTertiary,
    tertiaryContainer = CalendarDarkTertiaryContainer,
    onTertiaryContainer = CalendarDarkOnTertiaryContainer,
    error = CalendarError,
    errorContainer = CalendarErrorContainer,
    onError = CalendarOnError,
    onErrorContainer = CalendarOnErrorContainer,
    background = CalendarDarkBackground,
    onBackground = CalendarDarkOnBackground,
    surface = CalendarDarkSurface,
    onSurface = CalendarDarkOnSurface,
    surfaceVariant = CalendarDarkSurfaceVariant,
    onSurfaceVariant = CalendarDarkOnSurfaceVariant,
    outline = CalendarDarkOutline,
    inverseOnSurface = CalendarInverseOnSurface,
    inverseSurface = CalendarInverseSurface,
    inversePrimary = CalendarInversePrimary,
    surfaceTint = CalendarSurfaceTint,
    outlineVariant = CalendarDarkOutlineVariant,
    scrim = CalendarScrim,
)

private val LightColorScheme = lightColorScheme(
    primary = CalendarPrimary,
    onPrimary = CalendarOnPrimary,
    primaryContainer = CalendarPrimaryContainer,
    onPrimaryContainer = CalendarOnPrimaryContainer,
    secondary = CalendarSecondary,
    onSecondary = CalendarOnSecondary,
    secondaryContainer = CalendarSecondaryContainer,
    onSecondaryContainer = CalendarOnSecondaryContainer,
    tertiary = CalendarTertiary,
    onTertiary = CalendarOnTertiary,
    tertiaryContainer = CalendarTertiaryContainer,
    onTertiaryContainer = CalendarOnTertiaryContainer,
    error = CalendarError,
    errorContainer = CalendarErrorContainer,
    onError = CalendarOnError,
    onErrorContainer = CalendarOnErrorContainer,
    background = CalendarBackground,
    onBackground = CalendarOnBackground,
    surface = CalendarSurface,
    onSurface = CalendarOnSurface,
    surfaceVariant = CalendarSurfaceVariant,
    onSurfaceVariant = CalendarOnSurfaceVariant,
    outline = CalendarOutline,
    inverseOnSurface = CalendarInverseOnSurface,
    inverseSurface = CalendarInverseSurface,
    inversePrimary = CalendarInversePrimary,
    surfaceTint = CalendarSurfaceTint,
    outlineVariant = CalendarOutlineVariant,
    scrim = CalendarScrim,
)

@Composable
fun ModernCalendarTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    dynamicColor: Boolean = false, // Disabled to use our blue theme
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Use transparent status bar for modern edge-to-edge design
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CalendarTypography,
        shapes = CalendarShapes,
        content = content
    )
}
