package com.moderncalendar.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.data.repository.SettingsRepository
import com.moderncalendar.core.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    
    private val _settings = MutableStateFlow<Map<String, Any>>(emptyMap())
    val settings: StateFlow<Map<String, Any>> = _settings.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _theme = MutableStateFlow("light")
    val theme: StateFlow<String> = _theme.asStateFlow()
    
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()
    
    private val _reminderTime = MutableStateFlow(15) // minutes
    val reminderTime: StateFlow<Int> = _reminderTime.asStateFlow()
    
    private val _weekStartsOn = MutableStateFlow(1) // Sunday = 1
    val weekStartsOn: StateFlow<Int> = _weekStartsOn.asStateFlow()
    
    private val _timeFormat = MutableStateFlow("12") // 12 or 24 hour
    val timeFormat: StateFlow<String> = _timeFormat.asStateFlow()
    
    private val _dateFormat = MutableStateFlow("MM/dd/yyyy")
    val dateFormat: StateFlow<String> = _dateFormat.asStateFlow()
    
    init {
        loadSettings()
    }
    
    fun loadSettings() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Load theme preference
            userPreferencesRepository.getTheme().collect { theme ->
                _theme.value = theme
            }
            
            // Load notification settings
            userPreferencesRepository.getNotificationsEnabled().collect { enabled ->
                _notificationsEnabled.value = enabled
            }
            
            // Load reminder time
            userPreferencesRepository.getReminderTime().collect { time ->
                _reminderTime.value = time
            }
            
            // Load week start preference
            userPreferencesRepository.getWeekStartsOn().collect { day ->
                _weekStartsOn.value = day
            }
            
            // Load time format
            userPreferencesRepository.getTimeFormat().collect { format ->
                _timeFormat.value = format
            }
            
            // Load date format
            userPreferencesRepository.getDateFormat().collect { format ->
                _dateFormat.value = format
            }
            
            _isLoading.value = false
        }
    }
    
    fun updateTheme(theme: String) {
        viewModelScope.launch {
            _theme.value = theme
            userPreferencesRepository.setTheme(theme)
        }
    }
    
    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            _notificationsEnabled.value = enabled
            userPreferencesRepository.setNotificationsEnabled(enabled)
        }
    }
    
    fun updateReminderTime(minutes: Int) {
        viewModelScope.launch {
            _reminderTime.value = minutes
            userPreferencesRepository.setReminderTime(minutes)
        }
    }
    
    fun updateWeekStartsOn(day: Int) {
        viewModelScope.launch {
            _weekStartsOn.value = day
            userPreferencesRepository.setWeekStartsOn(day)
        }
    }
    
    fun updateTimeFormat(format: String) {
        viewModelScope.launch {
            _timeFormat.value = format
            userPreferencesRepository.setTimeFormat(format)
        }
    }
    
    fun updateDateFormat(format: String) {
        viewModelScope.launch {
            _dateFormat.value = format
            userPreferencesRepository.setDateFormat(format)
        }
    }
    
    fun resetToDefaults() {
        viewModelScope.launch {
            _isLoading.value = true
            
            userPreferencesRepository.resetToDefaults()
            
            _theme.value = "light"
            _notificationsEnabled.value = true
            _reminderTime.value = 15
            _weekStartsOn.value = 1
            _timeFormat.value = "12"
            _dateFormat.value = "MM/dd/yyyy"
            
            _isLoading.value = false
        }
    }
    
    fun exportSettings() {
        viewModelScope.launch {
            settingsRepository.exportSettings()
        }
    }
    
    fun importSettings(settingsData: String) {
        viewModelScope.launch {
            _isLoading.value = true
            settingsRepository.importSettings(settingsData)
            loadSettings() // Reload after import
        }
    }
}