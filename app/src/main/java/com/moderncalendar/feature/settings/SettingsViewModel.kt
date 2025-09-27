package com.moderncalendar.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.auth.AuthRepository
import com.moderncalendar.core.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                settingsRepository.isDarkMode,
                settingsRepository.isDynamicColors,
                settingsRepository.isEventRemindersEnabled
            ) { isDarkMode, isDynamicColors, isEventRemindersEnabled ->
                SettingsUiState(
                    isDarkMode = isDarkMode,
                    isDynamicColors = isDynamicColors,
                    isEventRemindersEnabled = isEventRemindersEnabled
                )
            }.collect { uiState ->
                _uiState.value = uiState
            }
        }
    }
    
    fun toggleDarkMode() {
        viewModelScope.launch {
            settingsRepository.setDarkMode(!_uiState.value.isDarkMode)
        }
    }
    
    fun toggleDynamicColors() {
        viewModelScope.launch {
            settingsRepository.setDynamicColors(!_uiState.value.isDynamicColors)
        }
    }
    
    fun toggleEventReminders() {
        viewModelScope.launch {
            settingsRepository.setEventRemindersEnabled(!_uiState.value.isEventRemindersEnabled)
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val isDynamicColors: Boolean = true,
    val isEventRemindersEnabled: Boolean = true
)
