package com.moderncalendar.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeManagerViewModel
    @Inject
    constructor(
        private val themeManager: ThemeManager,
    ) : ViewModel() {
        private val _currentTheme = MutableStateFlow(ThemeType.LIGHT)
        val currentTheme: StateFlow<ThemeType> = _currentTheme.asStateFlow()

        private val _isDarkMode = MutableStateFlow(false)
        val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

        private val _isDynamicColorsEnabled = MutableStateFlow(false)
        val isDynamicColorsEnabled: StateFlow<Boolean> = _isDynamicColorsEnabled.asStateFlow()

        private val _isHighContrastEnabled = MutableStateFlow(false)
        val isHighContrastEnabled: StateFlow<Boolean> = _isHighContrastEnabled.asStateFlow()

        init {
            loadThemeSettings()
        }

        fun getThemeManager(): ThemeManager = themeManager

        private fun loadThemeSettings() {
            viewModelScope.launch {
                _isDarkMode.value = themeManager.isDarkMode.value
                _currentTheme.value = if (themeManager.isDarkMode.value) ThemeType.DARK else ThemeType.LIGHT
                // _isDynamicColorsEnabled.value = themeManager.isDynamicColorsEnabled
                // _isHighContrastEnabled.value = themeManager.isHighContrastEnabled
            }
        }

        fun setTheme(theme: ThemeType) {
            viewModelScope.launch {
                themeManager.setTheme(theme)
                _currentTheme.value = theme
                _isDarkMode.value = theme == ThemeType.DARK
            }
        }

        fun toggleDarkMode() {
            viewModelScope.launch {
                val newTheme = if (_isDarkMode.value) ThemeType.LIGHT else ThemeType.DARK
                setTheme(newTheme)
            }
        }

        fun setDynamicColorsEnabled(enabled: Boolean) {
            viewModelScope.launch {
                themeManager.setDynamicColorsEnabled(enabled)
                _isDynamicColorsEnabled.value = enabled
            }
        }

        fun setHighContrastEnabled(enabled: Boolean) {
            viewModelScope.launch {
                themeManager.setHighContrastEnabled(enabled)
                _isHighContrastEnabled.value = enabled
            }
        }

        fun refreshThemeSettings() {
            loadThemeSettings()
        }
    }
