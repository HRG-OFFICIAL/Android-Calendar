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
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CalendarTypography,
        shapes = CalendarShapes,
        content = content
    )
}
