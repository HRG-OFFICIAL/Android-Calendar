package com.moderncalendar.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moderncalendar.core.common.repository.SettingsRepository
import com.moderncalendar.core.common.repository.UserPreferencesRepository
import com.moderncalendar.data.MockDataService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        private val userPreferencesRepository: UserPreferencesRepository,
        private val mockDataService: MockDataService,
    ) : ViewModel() {
        private val _settings = MutableStateFlow<Map<String, Any>>(emptyMap())
        val settings: StateFlow<Map<String, Any>> = _settings.asStateFlow()

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

        private val _theme = MutableStateFlow("light")
        val theme: StateFlow<String> = _theme.asStateFlow()

        private val _notificationsEnabled = MutableStateFlow(true)
        val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

        private val _dynamicColorsEnabled = MutableStateFlow(false)
        val dynamicColorsEnabled: StateFlow<Boolean> = _dynamicColorsEnabled.asStateFlow()

        private val _reminderTime = MutableStateFlow(15) // minutes
        val reminderTime: StateFlow<Int> = _reminderTime.asStateFlow()

        private val _weekStartsOn = MutableStateFlow(1) // Sunday = 1
        val weekStartsOn: StateFlow<Int> = _weekStartsOn.asStateFlow()

        private val _timeFormat = MutableStateFlow("12") // 12 or 24 hour
        val timeFormat: StateFlow<String> = _timeFormat.asStateFlow()

        private val _dateFormat = MutableStateFlow("MM/dd/yyyy")
        val dateFormat: StateFlow<String> = _dateFormat.asStateFlow()

        private val _mockDataMessage = MutableStateFlow<String?>(null)
        val mockDataMessage: StateFlow<String?> = _mockDataMessage.asStateFlow()

        init {
            loadSettings()
        }

        fun loadSettings() {
            viewModelScope.launch {
                _isLoading.value = true

                launch { userPreferencesRepository.getTheme().collect { _theme.value = it } }
                launch {
                    userPreferencesRepository.getUserSettings().collect { settings ->
                        _notificationsEnabled.value = settings.eventReminders
                        _weekStartsOn.value = settings.weekStartsOn.ordinal
                    }
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
                val currentSettings = userPreferencesRepository.getUserSettings().first()
                userPreferencesRepository.updateUserSettings(currentSettings.copy(eventReminders = enabled))
            }
        }

        fun updateReminderTime(minutes: Int) {
            viewModelScope.launch {
                _reminderTime.value = minutes
                // Note: UserSettings doesn't have reminderTime, this is just for UI state
            }
        }

        fun updateWeekStartsOn(day: Int) {
            viewModelScope.launch {
                _weekStartsOn.value = day
                val currentSettings = userPreferencesRepository.getUserSettings().first()
                val dayOfWeek =
                    com.moderncalendar.core.common.model.DayOfWeek.values().getOrElse(day) {
                        com.moderncalendar.core.common.model.DayOfWeek.MONDAY
                    }
                userPreferencesRepository.updateUserSettings(currentSettings.copy(weekStartsOn = dayOfWeek))
            }
        }

        fun updateTimeFormat(format: String) {
            viewModelScope.launch {
                _timeFormat.value = format
                // Note: UserSettings doesn't have timeFormat, this is just for UI state
            }
        }

        fun updateDateFormat(format: String) {
            viewModelScope.launch {
                _dateFormat.value = format
                // Note: UserSettings doesn't have dateFormat, this is just for UI state
            }
        }

        fun resetToDefaults() {
            viewModelScope.launch {
                _isLoading.value = true
                userPreferencesRepository.clearAllSettings()
                _theme.value = "system"
                _notificationsEnabled.value = true
                _reminderTime.value = 15
                _weekStartsOn.value = 1
                _timeFormat.value = "24h"
                _dateFormat.value = "dd/MM/yyyy"
                _isLoading.value = false
            }
        }

        fun addMockEvents() {
            viewModelScope.launch {
                _mockDataMessage.value = "Adding mock events..."
                mockDataService.addMockEventsIfNotExist()
                _mockDataMessage.value = "✅ Mock events added (duplicates skipped)!"

                // Clear message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _mockDataMessage.value = null
            }
        }

        fun resetMockDataFlag() {
            mockDataService.resetMockDataFlag()
            _mockDataMessage.value = "✅ Mock data flag reset!"

            viewModelScope.launch {
                // Clear message after 2 seconds
                kotlinx.coroutines.delay(2000)
                _mockDataMessage.value = null
            }
        }

        fun clearMockDataMessage() {
            _mockDataMessage.value = null
        }

        fun refreshMockEventsForToday() {
            viewModelScope.launch {
                _mockDataMessage.value = "Refreshing events for today..."
                mockDataService.refreshMockEventsForToday()
                _mockDataMessage.value = "✅ Mock events refreshed for today!"

                // Clear message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _mockDataMessage.value = null
            }
        }

        fun cleanupDuplicateEvents() {
            viewModelScope.launch {
                _mockDataMessage.value = "Cleaning up duplicate events..."
                mockDataService.cleanupDuplicateEvents()
                _mockDataMessage.value = "✅ Duplicate events cleaned up!"

                // Clear message after 3 seconds
                kotlinx.coroutines.delay(3000)
                _mockDataMessage.value = null
            }
        }
    }
