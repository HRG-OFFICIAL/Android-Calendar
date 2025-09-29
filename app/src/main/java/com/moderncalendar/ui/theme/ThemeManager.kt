package com.moderncalendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class ThemeType {
    LIGHT, DARK, SYSTEM
}

@Singleton
class ThemeManager @Inject constructor() {
    
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()
    
    private val _accentColor = MutableStateFlow(AccentColor.Teal)
    val accentColor: StateFlow<AccentColor> = _accentColor.asStateFlow()
    
    private val _themeStyle = MutableStateFlow(ThemeStyle.Material3)
    val themeStyle: StateFlow<ThemeStyle> = _themeStyle.asStateFlow()
    
    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }
    
    fun setDarkMode(isDark: Boolean) {
        _isDarkMode.value = isDark
    }
    
    fun setAccentColor(color: AccentColor) {
        _accentColor.value = color
    }
    
    fun setThemeStyle(style: ThemeStyle) {
        _themeStyle.value = style
    }
    
    fun setTheme(theme: ThemeType) {
        when (theme) {
            ThemeType.LIGHT -> _isDarkMode.value = false
            ThemeType.DARK -> _isDarkMode.value = true
            ThemeType.SYSTEM -> {
                // Note: isSystemInDarkTheme() requires @Composable context
                // This should be called from a @Composable function
                _isDarkMode.value = false // Default to light mode
            }
        }
    }
    
    fun setDynamicColorsEnabled(enabled: Boolean) {
        // This would be implemented based on system capabilities
    }
    
    fun setHighContrastEnabled(enabled: Boolean) {
        // This would be implemented based on system capabilities
    }
    
    @Composable
    fun getColorScheme(isDark: Boolean = _isDarkMode.value): ColorScheme {
        val accent = _accentColor.value
        return when (_themeStyle.value) {
            ThemeStyle.Material3 -> createMaterial3ColorScheme(isDark, accent)
            ThemeStyle.Material2 -> createMaterial2ColorScheme(isDark, accent)
            ThemeStyle.Custom -> createCustomColorScheme(isDark, accent)
        }
    }
    
    private fun createMaterial3ColorScheme(isDark: Boolean, accent: AccentColor): ColorScheme {
        val colors = accent.colors
        return if (isDark) {
            darkColorScheme(
                primary = colors.primary,
                secondary = colors.secondary,
                tertiary = colors.tertiary,
                surface = colors.surface,
                background = colors.background,
                error = colors.error
            )
        } else {
            lightColorScheme(
                primary = colors.primary,
                secondary = colors.secondary,
                tertiary = colors.tertiary,
                surface = colors.surface,
                background = colors.background,
                error = colors.error
            )
        }
    }
    
    private fun createMaterial2ColorScheme(isDark: Boolean, accent: AccentColor): ColorScheme {
        // Material 2 color scheme implementation
        val colors = accent.colors
        return if (isDark) {
            darkColorScheme(
                primary = colors.primary,
                secondary = colors.secondary,
                surface = colors.surface,
                background = colors.background
            )
        } else {
            lightColorScheme(
                primary = colors.primary,
                secondary = colors.secondary,
                surface = colors.surface,
                background = colors.background
            )
        }
    }
    
    private fun createCustomColorScheme(isDark: Boolean, accent: AccentColor): ColorScheme {
        // Custom color scheme implementation
        val colors = accent.colors
        return if (isDark) {
            darkColorScheme(
                primary = colors.primary,
                secondary = colors.secondary,
                tertiary = colors.tertiary,
                surface = colors.surface,
                background = colors.background,
                error = colors.error
            )
        } else {
            lightColorScheme(
                primary = colors.primary,
                secondary = colors.secondary,
                tertiary = colors.tertiary,
                surface = colors.surface,
                background = colors.background,
                error = colors.error
            )
        }
    }
}

enum class AccentColor(val colors: ColorPalette) {
    Purple(ColorPalette(
        primary = Color(0xFF6750A4),
        secondary = Color(0xFF625B71),
        tertiary = Color(0xFF7D5260),
        surface = Color(0xFF1C1B1F),
        background = Color(0xFF1C1B1F),
        error = Color(0xFFBA1A1A)
    )),
    Blue(ColorPalette(
        primary = Color(0xFF2196F3),
        secondary = Color(0xFF03DAC6),
        tertiary = Color(0xFF00BCD4),
        surface = Color(0xFFF5F5F5),
        background = Color(0xFFFFFFFF),
        error = Color(0xFFE57373)
    )),
    Green(ColorPalette(
        primary = Color(0xFF388E3C),
        secondary = Color(0xFF424242),
        tertiary = Color(0xFF388E3C),
        surface = Color(0xFF1E1E1E),
        background = Color(0xFF1E1E1E),
        error = Color(0xFFBA1A1A)
    )),
    Orange(ColorPalette(
        primary = Color(0xFFF57C00),
        secondary = Color(0xFF424242),
        tertiary = Color(0xFFF57C00),
        surface = Color(0xFF1E1E1E),
        background = Color(0xFF1E1E1E),
        error = Color(0xFFBA1A1A)
    )),
    Red(ColorPalette(
        primary = Color(0xFFD32F2F),
        secondary = Color(0xFF424242),
        tertiary = Color(0xFFD32F2F),
        surface = Color(0xFF1E1E1E),
        background = Color(0xFF1E1E1E),
        error = Color(0xFFBA1A1A)
    )),
    Teal(ColorPalette(
        primary = Color(0xFF009688),
        secondary = Color(0xFF4DB6AC),
        tertiary = Color(0xFF26A69A),
        surface = Color(0xFFF8F9FA),
        background = Color(0xFFFFFFFF),
        error = Color(0xFFE57373)
    ))
}

enum class ThemeStyle {
    Material3,
    Material2,
    Custom
}

data class ColorPalette(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val surface: Color,
    val background: Color,
    val error: Color
)
