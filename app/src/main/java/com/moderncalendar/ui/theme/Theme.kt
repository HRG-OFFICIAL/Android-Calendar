package com.moderncalendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.moderncalendar.core.analytics.AnalyticsManager
import javax.inject.Inject

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260)
)

@Composable
fun ModernCalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun ModernCalendarThemeWithManager(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeManager: ThemeManager = hiltViewModel<ThemeManagerViewModel>().themeManager,
    content: @Composable () -> Unit
) {
    val isDarkMode by themeManager.isDarkMode.collectAsState()
    val accentColor by themeManager.accentColor.collectAsState()
    val themeStyle by themeManager.themeStyle.collectAsState()
    
    val colorScheme = themeManager.getColorScheme(isDarkMode)
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = when (themeStyle) {
            ThemeStyle.Material3 -> MaterialTheme.typography
            ThemeStyle.Material2 -> MaterialTheme.typography
            ThemeStyle.Custom -> MaterialTheme.typography
        },
        content = content
    )
}

@Composable
fun ThemeSettingsScreen(
    themeManager: ThemeManager = hiltViewModel<ThemeManagerViewModel>().themeManager
) {
    val isDarkMode by themeManager.isDarkMode.collectAsState()
    val accentColor by themeManager.accentColor.collectAsState()
    val themeStyle by themeManager.themeStyle.collectAsState()
    
    Column {
        // Dark mode toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode")
            Switch(
                checked = isDarkMode,
                onCheckedChange = { themeManager.toggleDarkMode() }
            )
        }
        
        // Accent color selection
        Text("Accent Color")
        Row {
            AccentColor.values().forEach { color ->
                ColorButton(
                    color = color.colors.primary,
                    isSelected = accentColor == color,
                    onClick = { themeManager.setAccentColor(color) }
                )
            }
        }
        
        // Theme style selection
        Text("Theme Style")
        Row {
            ThemeStyle.values().forEach { style ->
                Button(
                    onClick = { themeManager.setThemeStyle(style) },
                    enabled = themeStyle != style
                ) {
                    Text(style.name)
                }
            }
        }
    }
}

@Composable
private fun ColorButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) color else Color.Transparent
        )
    ) {
        Text("")
    }
}
