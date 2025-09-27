package com.moderncalendar.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val dataStore = context.dataStore
    
    private val isDarkModeKey = booleanPreferencesKey("is_dark_mode")
    private val isDynamicColorsKey = booleanPreferencesKey("is_dynamic_colors")
    private val isEventRemindersEnabledKey = booleanPreferencesKey("is_event_reminders_enabled")
    
    val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isDarkModeKey] ?: false
    }
    
    val isDynamicColors: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isDynamicColorsKey] ?: true
    }
    
    val isEventRemindersEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isEventRemindersEnabledKey] ?: true
    }
    
    suspend fun setDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[isDarkModeKey] = isDarkMode
        }
    }
    
    suspend fun setDynamicColors(isDynamicColors: Boolean) {
        dataStore.edit { preferences ->
            preferences[isDynamicColorsKey] = isDynamicColors
        }
    }
    
    suspend fun setEventRemindersEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[isEventRemindersEnabledKey] = isEnabled
        }
    }
}
